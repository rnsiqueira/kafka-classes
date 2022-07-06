package br.com.rns.messages;

import br.com.rns.service.consumer.GsonDeserializer;
import br.com.rns.model.Message;
import br.com.rns.service.consumer.KafkaServiceConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;
import java.util.regex.Pattern;

public class LogsConsumer {


    public static void main(String[] args) throws InterruptedException {
        LogsConsumer logsConsumer = new LogsConsumer();
        KafkaServiceConsumer kafkaServiceConsumer = new KafkaServiceConsumer(Pattern.compile("produ.*||info.*||send.*||user.*||dead.*"), LogsConsumer::parse, LogsConsumer.class.getSimpleName(),
                Map.of(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName()));
        kafkaServiceConsumer.run();


    }

    private static void parse(ConsumerRecord<String, Message<Object>> record) {
        System.out.println("Processing Log service consumer");
        System.out.println(record.key());
        System.out.println(record.value().getPayload() + " " + record.value().getCorrelationId());
        System.out.println(record.partition());
        System.out.println(record.offset());

    }
}
