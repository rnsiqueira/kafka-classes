package br.com.rns.messages;

import br.com.rns.model.GsonDeserializer;
import br.com.rns.model.Message;
import br.com.rns.model.Order;
import br.com.rnsiquera.service.KafkaService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;
import java.util.regex.Pattern;

public class LogsConsumer {


    public static void main(String[] args) throws InterruptedException {
        LogsConsumer logsConsumer = new LogsConsumer();
        KafkaService kafkaService = new KafkaService(Pattern.compile("produ.*||info.*"), LogsConsumer::parse, LogsConsumer.class.getSimpleName()
                , Object.class,
                Map.of(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName()));
        kafkaService.run();


    }

    private static void parse(ConsumerRecord<String, Message<Object>> record) {
        System.out.println("Processing Log service consumer");
        System.out.println(record.key());
        System.out.println(record.value().getPayload());
        System.out.println(record.partition());
        System.out.println(record.offset());

    }
}
