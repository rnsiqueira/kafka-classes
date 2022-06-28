package br.com.rnsiquera.action;

import br.com.rnsiquera.model.Order;
import br.com.rnsiquera.service.KafkaDispatcher;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class newOrderMain {

    private static KafkaProducer<String, String> producer;
    private static ProducerRecord<String, String> products;
    private static ProducerRecord<String, String> information;

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        KafkaDispatcher products = new KafkaDispatcher<Order>();
        KafkaDispatcher informations = new KafkaDispatcher<String>();


        for (int i = 0; i < 25; i++) {
            var userid = UUID.randomUUID().toString();
            var orderId = UUID.randomUUID().toString();
            var amount = new BigDecimal(Math.random() * 5000 + 1);
            Order order = new Order(userid, orderId, amount);
            products.send("products", userid, order);
            informations.send("information", userid, "New Information");


        }

    }
}
