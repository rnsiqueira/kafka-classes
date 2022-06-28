package br.com.rnsiquera.config;

import br.com.rnsiquera.model.GsonSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Closeable;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaDispatcher<T> implements Closeable {


    private KafkaProducer<String, T> producer = new KafkaProducer<>(properties());
    private ProducerRecord<String, T> record;

    public static Properties properties() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GsonSerializer.class.getName());
        return properties;
    }

    public void send(String topic, String key, T message) throws ExecutionException, InterruptedException {
        record = new ProducerRecord<>(topic, key, message);
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