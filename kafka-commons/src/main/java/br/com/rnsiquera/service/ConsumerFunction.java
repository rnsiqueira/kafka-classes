package br.com.rnsiquera.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.concurrent.ExecutionException;

public interface ConsumerFunction<T> {
    void consumer(ConsumerRecord<String, T> record) throws InterruptedException, ExecutionException;
}
