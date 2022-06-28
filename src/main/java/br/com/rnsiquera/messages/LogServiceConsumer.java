package br.com.rnsiquera.messages;

import br.com.rnsiquera.config.KafkaService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Map;
import java.util.regex.Pattern;

public class LogServiceConsumer {


    public static void main(String[] args) throws InterruptedException {
        LogServiceConsumer logServiceConsumer = new LogServiceConsumer();
        KafkaService kafkaService = new KafkaService(Pattern.compile("produ.*||info.*"), LogServiceConsumer::parse, LogServiceConsumer.class.getName()
                , String.class, Map.of(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()));
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
