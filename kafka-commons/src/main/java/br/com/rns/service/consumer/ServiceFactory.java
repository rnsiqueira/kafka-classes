package br.com.rns.service.consumer;

public interface ServiceFactory<T> {
    ConsumerService<T> create() throws Exception;
}
