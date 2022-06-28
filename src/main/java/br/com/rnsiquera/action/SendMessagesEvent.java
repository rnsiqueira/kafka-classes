package br.com.rnsiquera.action;

import br.com.rnsiquera.action.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SendMessagesEvent {

    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    @Scheduled(cron = "*/10 * * * * ?")
    public void send() {

        kafkaTemplate.send("CreateProject", Event.builder().id(1L).name("Test").message("Send event test!").build());
        kafkaTemplate.send("stream-topic-test", Event.builder().id(1L).name("Test").message("Send event test!").build());
        System.out.println("Sent Messages!");

    }
}
