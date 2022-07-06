package br.com.rns.service;

import br.com.rns.model.Message;
import br.com.rns.model.Order;
import br.com.rns.service.consumer.ConsumerService;
import br.com.rns.service.consumer.ServiceRunner;
import br.com.rns.service.dispatcher.KafkaDispatcher;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.concurrent.ExecutionException;

public class SendEmails implements ConsumerService<Order> {

    private KafkaDispatcher<String> emailDispacher = new KafkaDispatcher<>();

    public static void main(String[] args) {
        new ServiceRunner(SendEmails::new).start(5);
    }

    @Override
    public void parse(ConsumerRecord<String, Message<Order>> record) throws ExecutionException, InterruptedException {
        System.out.println("Sending emails for Order!");
        var order = record.value().getPayload();
        System.out.println("ID trace: " + record.value().getCorrelationId());
        System.out.println("Order: " + order);
        emailDispacher.send(getTopic(), String.valueOf(record.value().getCorrelationId()), order.getEmail(),
                record.value().getCorrelationId().continueIdWith(SendEmails.class.getSimpleName()));


    }

    @Override
    public String getTopic() {
        return "products";
    }

    @Override
    public String getGroup() {
        return SendEmails.class.getSimpleName();
    }
}
