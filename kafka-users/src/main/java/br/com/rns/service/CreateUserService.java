package br.com.rns.service;

import br.com.rns.model.Message;
import br.com.rns.model.Order;
import br.com.rns.service.consumer.ConsumerService;
import br.com.rns.service.consumer.ServiceRunner;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.*;
import java.util.UUID;

public class CreateUserService implements ConsumerService<Order> {

    private final Connection connection;

    public CreateUserService() throws SQLException {

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
        new ServiceRunner(CreateUserService::new).start(1);


    }

    public void parse(ConsumerRecord<String, Message<Order>> record) throws SQLException {
        System.out.println("-------------------");
        System.out.println("Processing new order, checking for new user");
        System.out.println(record.value().getPayload());
        Order order = record.value().getPayload();
        if (isNewuser(order.getEmail())) {
            insertNewUser(order.getEmail());


        }


    }

    @Override
    public String getTopic() {
        return "products";
    }

    @Override
    public String getGroup() {
        return CreateUserService.class.getSimpleName();
    }

    private void insertNewUser(String email) throws SQLException {
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
