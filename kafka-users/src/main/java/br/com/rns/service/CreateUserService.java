package br.com.rns.service;

import br.com.rns.model.GsonDeserializer;
import br.com.rns.model.Order;
import br.com.rnsiquera.service.KafkaService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.*;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

public class CreateUserService {

    private final Connection connection;

    public CreateUserService() throws SQLException {

        String url = "jdbc:sqlite:target/sql_database.db";
        connection = DriverManager.getConnection(url);
        try {
            connection.createStatement().execute("create table users (uuid varchar(255) primary key," +
                    "email varchar(255))");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }

    public static void main(String[] args) throws SQLException, InterruptedException {
        CreateUserService createUserService = new CreateUserService();
        KafkaService products = new KafkaService(Arrays.asList("products"), createUserService::parse, CreateUserService.class.getName(), Order.class, Map.of(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName()));

        products.run();


    }

    private void parse(ConsumerRecord<String, Order> record) throws SQLException {
        System.out.println("-------------------");
        System.out.println("Processing new order, checking for new user");
        System.out.println(record.value());
        Order order = record.value();
        if (isNewuser(order.getEmail())) {
            insertNewUser(order.getEmail());


        }


    }

    private void insertNewUser( String email) throws SQLException {
        PreparedStatement sql = connection.prepareStatement("insert into users (uuid,email) " +
                "values (?,?)");

        sql.setString(1, UUID.randomUUID().toString());
        sql.setString(2, email);
        sql.execute();

    }

    private boolean isNewuser(String email) throws SQLException {
        PreparedStatement sql = connection.prepareStatement("select * from users where email = ? " +
                "limit 1");
        sql.setString(1, email);
        ResultSet resultSet = sql.executeQuery();

        return !resultSet.next();
    }


}
