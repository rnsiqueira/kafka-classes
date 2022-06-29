package br.com.rnsiquera.service;

import br.com.rns.model.GsonDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class KafkaService<T> implements Closeable {

    private final KafkaConsumer<String, T> consumer;
    private final ConsumerFunction parse;

    public KafkaService(List<String> topics, ConsumerFunction parse, String group, Class<T> type, Map<String, String> properties) {
        this.consumer = new KafkaConsumer<>(getProperties(group, type, properties));
        this.consumer.subscribe(topics);
        this.parse = parse;


    }

    public KafkaService(Pattern topics, ConsumerFunction parse, String group, Class<T> type, Map<String, String> properties) {
        this.consumer = new KafkaConsumer<>(getProperties(group, type, properties));
        this.consumer.subscribe(topics);
        this.parse = parse;


    }

    public void run() throws InterruptedException {

        while (true) {
            var records = this.consumer.poll(Duration.ofMillis(100));
            if (!records.isEmpty()) {
                for (var r : records) {
                    try {
                        parse.consumer(r);
                    } catch (ExecutionException e) {
                        System.out.println(e.getMessage());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }


    }

    private Properties getProperties(String group, Class<T> type, Map<String, String> overrideProperties) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9090");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, group);
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 5);
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
        properties.put(GsonDeserializer.TYPE_CONFIG, type.getName());
        properties.putAll(overrideProperties);
        return properties;
    }

    @Override
    public void close() throws IOException {

        this.consumer.close();


    }
}
