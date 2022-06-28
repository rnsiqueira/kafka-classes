package br.com.rnsiquera.config;


import br.com.rnsiquera.model.Event;
import com.google.gson.Gson;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    @KafkaListener(topics = "CreateProject", groupId = "c1", id = "101")
    void Listener(String data) {
        Event event = new Gson().fromJson(data, Event.class);


        System.out.println("Listener received: " + event.toString() + " !!!!");


    }
}
