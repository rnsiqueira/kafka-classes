package br.com.rns.model;

import java.util.UUID;

public class CorrelationId {

    private final String id;

    public CorrelationId(String idenfy) {
        this.id = idenfy + "(" + UUID.randomUUID().toString() + ")";
    }


    @Override
    public String toString() {
        return "CorrelationId{" +
                "id='" + id + '\'' +
                '}';
    }

    public CorrelationId continueIdWith(String correlationId) {
        return new CorrelationId(id + "-" + correlationId);
    }
}
