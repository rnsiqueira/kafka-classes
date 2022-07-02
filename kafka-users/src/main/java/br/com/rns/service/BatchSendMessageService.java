package br.com.rns.service;

import br.com.rns.model.Message;
import br.com.rns.model.User;
import br.com.rnsiquera.service.KafkaDispatcher;
import br.com.rnsiquera.service.KafkaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class BatchSendMessageService {

    private final Connection connection;
    private KafkaDispatcher<User> userDispatcher = new KafkaDispatcher<>();

    public BatchSendMessageService() throws SQLException {
        String url = "jdbc:sqlite:kafka-users/sql_database.db";
        connection = DriverManager.getConnection(url);
        try {
            connection.createStatement().execute("create table users (uuid varchar(255) primary key," +
                    "email varchar(255))");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws SQLException, InterruptedException {
        var batchSendMessageService = new BatchSendMessageService();
        KafkaService batch_service = new KafkaService(Arrays.asList("send_message_to_all_users"), batchSendMessageService::parse,
                BatchSendMessageService.class.getSimpleName(),
                String.class, Map.of());
        batch_service.run();
    }

    private void parse(ConsumerRecord<String, Message<String>> record) throws ExecutionException, InterruptedException, SQLException {
        System.out.println("--------------------");
        System.out.println("Processing new batch");
        System.out.println("Topic: "+record.value().getPayload());
        for (User user : getAllUsers()) {
            String topic = record.value().getPayload();
            System.out.println(topic);
            userDispatcher.send(record.value().getPayload(), user.getId(), user);
        }
    }

    private List<User> getAllUsers() throws SQLException {
        PreparedStatement sql = connection.prepareStatement("select uuid from users");
        ResultSet sqlValues = sql.executeQuery();
        ArrayList<User> users = new ArrayList<>();
        while (sqlValues.next()) {
            users.add(new User(sqlValues.getString(1)));
        }

        return users;

    }
}
