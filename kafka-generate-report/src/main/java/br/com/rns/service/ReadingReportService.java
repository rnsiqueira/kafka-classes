package br.com.rns.service;

import br.com.rns.model.Message;
import br.com.rns.model.User;
import br.com.rns.service.consumer.ConsumerService;
import br.com.rns.service.consumer.ServiceRunner;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ReadingReportService implements ConsumerService<User> {
    private static final Path SOURCE = new File("kafka-generate-report/src/main/resources/report.txt").toPath();

    public static void main(String[] args) {
        new ServiceRunner(ReadingReportService::new).start(5);
    }

    public void parse(ConsumerRecord<String, Message<User>> record) throws IOException {
        System.out.println("-----------------");
        System.out.println("Processing report for: " + record.value().getPayload());
        User user = record.value().getPayload();
        var target = new File(user.getReportPath());
        IO.copyTo(SOURCE, target);
        IO.append(target, "Create for " + String.valueOf(user.getId()));
        System.out.println("Report Created in: " + target.getAbsolutePath());


    }

    @Override
    public String getTopic() {
        return "user_generate_reading_report";
    }

    @Override
    public String getGroup() {
        return ReadingReportService.class.getSimpleName();
    }
}
