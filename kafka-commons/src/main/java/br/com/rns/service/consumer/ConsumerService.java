package br.com.rns.service.consumer;

import br.com.rns.model.Message;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public interface ConsumerService<T> {

    void parse(ConsumerRecord<String, Message<T>> record) throws ExecutionException, InterruptedException, IOException, SQLException;

    String getTopic();

    String getGroup();
}
