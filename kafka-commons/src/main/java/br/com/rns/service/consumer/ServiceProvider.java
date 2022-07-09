package br.com.rns.service.consumer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Callable;

public class ServiceProvider<T> implements Callable<Void> {

    private final ServiceFactory<T> factory;

    public ServiceProvider(ServiceFactory<T> factory) {
        this.factory = factory;
    }


    public Void call() throws Exception {
        var myService = factory.create();
        try (var service = new KafkaServiceConsumer(Arrays.asList(myService.getTopic()), myService::parse,
                myService.getGroup(), Map.of())) {
            service.run();
        }
        return null;

    }
}
