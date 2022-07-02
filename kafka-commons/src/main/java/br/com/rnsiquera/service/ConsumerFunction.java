package br.com.rnsiquera.service;

import br.com.rns.model.Message;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ConsumerFunction<T> {
    void consumer(ConsumerRecord<String, Message<T>> record) throws Exception;
}
