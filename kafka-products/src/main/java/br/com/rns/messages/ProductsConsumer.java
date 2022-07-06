package br.com.rns.messages;

import br.com.rns.service.consumer.GsonDeserializer;
import br.com.rns.model.Message;
import br.com.rns.model.Order;
import br.com.rns.service.dispatcher.KafkaDispatcher;
import br.com.rns.service.consumer.KafkaServiceConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ProductsConsumer {


    public static void main(String[] args) throws InterruptedException {
        ProductsConsumer serviceConsumer = new ProductsConsumer();
        KafkaServiceConsumer products = new KafkaServiceConsumer(Arrays.asList("products"),
                serviceConsumer::parse,
                ProductsConsumer.class.getSimpleName(),
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
            dispatcher.send("product_fraud", order.getEmail(), order, record.value().getCorrelationId().continueIdWith(ProductsConsumer.class.getSimpleName()));
        } else {
            System.out.println("Product is Ok!");
            System.out.println(order);
            dispatcher.send("product_sent", order.getEmail(), order, record.value().getCorrelationId().continueIdWith(ProductsConsumer.class.getSimpleName()));
        }


    }

    private boolean isFraud(Order order) {
        return order.getAmount().compareTo(new BigDecimal("6500")) >= 0;
    }

    private KafkaDispatcher dispatcher = new KafkaDispatcher();


}
