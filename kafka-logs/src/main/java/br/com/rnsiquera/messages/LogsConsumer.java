package br.com.rnsiquera.messages;

import br.com.rnsiquera.service.KafkaService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Map;
import java.util.regex.Pattern;

public class LogsConsumer {


    public static void main(String[] args) throws InterruptedException {
        LogsConsumer logsConsumer = new LogsConsumer();
        KafkaService kafkaService = new KafkaService(Pattern.compile("produ.*||info.*"), LogsConsumer::parse, LogsConsumer.class.getName()
                , String.class,
                Map.of(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()));
        kafkaService.run();


    }

    private static void parse(ConsumerRecord<String, String> record) {
        System.out.println("Processing Log service consumer");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());

    }
}
