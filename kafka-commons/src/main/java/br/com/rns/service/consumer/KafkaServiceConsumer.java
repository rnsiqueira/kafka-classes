package br.com.rns.service.consumer;

import br.com.rns.model.Message;
import br.com.rns.service.ConsumerFunction;
import br.com.rns.service.dispatcher.KafkaDispatcher;
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

public class KafkaServiceConsumer<T> implements Closeable {

    private final KafkaConsumer<String, Message<T>> consumer;
    private final ConsumerFunction parse;

    public KafkaServiceConsumer(List<String> topics, ConsumerFunction<T> parse, String group, Map<String, String> properties) {
        this.consumer = new KafkaConsumer<>(getProperties(group, properties));
        this.consumer.subscribe(topics);
        this.parse = parse;


    }

    public KafkaServiceConsumer(Pattern topics, ConsumerFunction<T> parse, String group, Map<String, String> properties) {
        this.consumer = new KafkaConsumer<>(getProperties(group, properties));
        this.consumer.subscribe(topics);
        this.parse = parse;


    }

    public void run() throws InterruptedException {
        var captureError = new KafkaDispatcher();
        while (true) {

            var records = this.consumer.poll(Duration.ofMillis(100));
            if (!records.isEmpty()) {
                for (var r : records) {

                    try {
                        parse.consumer(r);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        try {
                            captureError.send("dead_letter", r.value().getCorrelationId().toString(), r.value().getPayload(),
                                    r.value().getCorrelationId().continueIdWith("dead_letter"));
                        } catch (ExecutionException ee) {
                           System.exit(1);
                        } catch (InterruptedException ie) {
                            System.exit(1);
                        }
                    }


                }
            }


        }


    }

    private Properties getProperties(String group, Map<String, String> overrideProperties) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9090");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, group);
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 5);
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.putAll(overrideProperties);
        return properties;
    }

    @Override
    public void close() throws IOException {

        this.consumer.close();


    }
}
