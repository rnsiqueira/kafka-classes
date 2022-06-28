package br.com.rnsiquera.messages;

import br.com.rnsiquera.config.KafkaService;
import br.com.rnsiquera.model.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Arrays;
import java.util.Map;

public class ServiceConsumer {


    public static void main(String[] args) throws InterruptedException {
        ServiceConsumer serviceConsumer = new ServiceConsumer();
        KafkaService products = new KafkaService(Arrays.asList("products"), serviceConsumer::parse, ServiceConsumer.class.getName()
                , Order.class, Map.of());

        products.run();


    }

    private void parse(ConsumerRecord<String, Order> record) {

        System.out.println("Processing Service Consumer");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());


    }


}
