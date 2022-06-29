package br.com.rns.model;

public class User {

    private final String id;

    public User(String id) {
        this.id = id;
    }

    public String getReportPath() {
        return "kafka-generate-report/src/main/resources/userReport/" + id + ".txt";
    }

    public String getId() {
        return this.id;
    }
}
