package br.com.rnsiquera.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class kafkaTopicConfig {


    @Autowired
    public NewTopic aTopic() {
        return TopicBuilder.name("CreateProject").partitions(5).build();
    }

    @Autowired
    public NewTopic topic2() {
        return TopicBuilder.name("stream-topic-test").partitions(5).build();
    }



}
