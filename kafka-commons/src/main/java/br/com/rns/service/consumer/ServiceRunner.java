package br.com.rns.service.consumer;

import java.util.concurrent.Executors;

public class ServiceRunner<T> {
    private final ServiceProvider<T> provider;

    public ServiceRunner(ServiceFactory<T> factory) {
        this.provider = new ServiceProvider<>(factory);
    }

    public void start(int thredCount) {
        var pool = Executors.newFixedThreadPool(thredCount);
        for (int i = 0; i <= thredCount; i++) {
            pool.submit(provider);
        }
    }
}
