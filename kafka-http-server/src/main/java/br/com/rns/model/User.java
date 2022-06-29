package br.com.rns.model;

public class User {

    private final Long id;

    public User(Long id) {
        this.id = id;
    }

    public String getReportPath() {
        return "src/resources/userReport/"+String.valueOf(id)+".txt";
    }

    public Long getId() {
        return this.id;
    }
}
