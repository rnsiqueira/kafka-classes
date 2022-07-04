package br.com.rnsiquera.action;

import br.com.rns.model.CorrelationId;
import br.com.rns.model.Order;
import br.com.rnsiquera.service.KafkaDispatcher;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {


    public static void main(String[] args) throws ExecutionException, InterruptedException {

        KafkaDispatcher<Order> products = new KafkaDispatcher<>();
        KafkaDispatcher<String> informations = new KafkaDispatcher<>();

        String email = Math.random() + "@email.com";
        for (int i = 0; i < 25; i++) {
            var orderId = UUID.randomUUID().toString();
            var amount = new BigDecimal(Math.random() * 5000 + 1);

            Order order = new Order(orderId, amount, email);
            products.send("products", order.getEmail(), order, new CorrelationId(NewOrderMain.class.getSimpleName()));
            informations.send("information", order.getEmail(), "New Information", new CorrelationId(NewOrderMain.class.getSimpleName()));


        }

    }
}
