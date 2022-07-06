package br.com.rns.service.dispatcher;

import br.com.rns.model.CorrelationId;
import br.com.rns.model.Message;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Closeable;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class KafkaDispatcher<T> implements Closeable {


    private KafkaProducer<String, Message<T>> producer;

    public KafkaDispatcher() {
        producer = new KafkaProducer<>(properties());
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
        Future<RecordMetadata> feature = sendAsync(topic, key, payload, correlationId);
        feature.get();

    }

    public Future<RecordMetadata> sendAsync(String topic, String key, T payload, CorrelationId correlationId) {
        var message = new Message<>(correlationId.continueIdWith("_"+topic), payload);
        var record = new ProducerRecord<>(topic, key, message);
        Callback callback = (data, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
            }
            System.out.println("Sent successfully Topic: " + data.topic() + " OffSet: " + data.offset() + " Partition: " + data.partition());
        };

        var feature = producer.send(record, callback);
        return feature;
    }


    @Override
    public void close() throws IOException {
        producer.close();

    }
}
