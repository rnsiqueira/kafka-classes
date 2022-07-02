package br.com.rns.service;

import br.com.rns.model.GsonDeserializer;
import br.com.rns.model.Message;
import br.com.rns.model.User;
import br.com.rnsiquera.service.KafkaService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;

public class ReadingReportService {
    private static final Path SOURCE = new File("kafka-generate-report/src/main/resources/report.txt").toPath();

    public static void main(String[] args) throws InterruptedException {
        var readingReportService = new ReadingReportService();
        KafkaService report_service = new KafkaService(Arrays.asList("user_generate_reading_report"),
                readingReportService::parse,
                ReadingReportService.class.getSimpleName(),
                User.class,
                Map.of(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName()));
        report_service.run();
    }

    private void parse(ConsumerRecord<String, Message<User>> record) throws IOException {
        System.out.println("-----------------");
        System.out.println("Processing report for: " + record.value().getPayload());
        User user = record.value().getPayload();
        var target = new File(user.getReportPath());
        IO.copyTo(SOURCE, target);
        IO.append(target, "Create for " + String.valueOf(user.getId()));
        System.out.println("Report Created in: " + target.getAbsolutePath());


    }
}
