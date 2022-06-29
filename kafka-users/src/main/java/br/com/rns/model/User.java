package br.com.rns.model;

public class User {

    private final String id;

    public User(String id) {
        this.id = id;
    }

    public String getReportPath() {
        return "src/resources/userReport/" + String.valueOf(id) + ".txt";
    }

    public String getId() {
        return this.id;
    }
}
