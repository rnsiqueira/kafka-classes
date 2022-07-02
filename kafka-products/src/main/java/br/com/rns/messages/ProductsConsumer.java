package br.com.rns.messages;

import br.com.rns.model.GsonDeserializer;
import br.com.rns.model.Message;
import br.com.rns.model.Order;
import br.com.rnsiquera.service.KafkaDispatcher;
import br.com.rnsiquera.service.KafkaService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ProductsConsumer {


    public static void main(String[] args) throws InterruptedException {
        ProductsConsumer serviceConsumer = new ProductsConsumer();
        KafkaService products = new KafkaService(Arrays.asList("products"),
                serviceConsumer::parse,
                ProductsConsumer.class.getSimpleName(),
                Order.class,
                Map.of(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName()));

        products.run();


    }

    private void parse(ConsumerRecord<String, Message<Order>> record) throws ExecutionException, InterruptedException {


        System.out.println("----------Receding products-------------");
        System.out.println(record.key());
        System.out.println(record.value().getPayload());
        System.out.println(record.partition());
        System.out.println(record.offset());
        Order order = record.value().getPayload();

        if (isFraud(order)) {
            System.out.println("Product is with amount invalid!!!!!");
            System.out.println(order);
            dispatcher.send("product_fraud", order.getEmail(), order);
        } else {
            System.out.println("Product is Ok!");
            System.out.println(order);
            dispatcher.send("product_sent", order.getEmail(), order);
        }


    }

    private boolean isFraud(Order order) {
        return order.getAmount().compareTo(new BigDecimal("6500")) >= 0;
    }

    private KafkaDispatcher dispatcher = new KafkaDispatcher();


}
