package br.com.rnsiquera.action;

import br.com.rns.model.Order;
import br.com.rnsiquera.service.KafkaDispatcher;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class newOrderMain {

    private static KafkaProducer<String, String> producer;
    private static ProducerRecord<String, Order> products;
    private static ProducerRecord<String, String> information;

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        KafkaDispatcher products = new KafkaDispatcher<>();
        KafkaDispatcher informations = new KafkaDispatcher<>();

        String email = Math.random() + "@email.com";
        for (int i = 0; i < 25; i++) {
            var orderId = UUID.randomUUID().toString();
            var amount = new BigDecimal(Math.random() * 5000 + 1);

            Order order = new Order(orderId, amount, email);
            products.send("products", order.getEmail(), order);
            informations.send("information", order.getEmail(), "New Information");


        }

    }
}
