package br.com.rnsiquera.service;

import br.com.rns.model.CorrelationId;
import br.com.rns.model.GsonSerializer;
import br.com.rns.model.Message;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Closeable;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaDispatcher<T> implements Closeable {


    private KafkaProducer<String, Message<T>> producer;

    public KafkaDispatcher() {
        producer =  new KafkaProducer<>(properties());
    }

    public static Properties properties() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9090");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GsonSerializer.class.getName());
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        return properties;
    }

    public void send(String topic, String key, T payload, CorrelationId correlationId) throws ExecutionException, InterruptedException {
        var message = new Message<>(correlationId, payload);
        var record = new ProducerRecord<>(topic, key, message);
        producer.send(record, (data, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
            }
            System.out.println("Topic: " + data.topic() + " OffSet: " + data.offset() + " Partition: " + data.partition());

        }).get();

    }

    @Override
    public void close() throws IOException {
        producer.close();

    }
}
