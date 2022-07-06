package br.com.rns.service.consumer;

import br.com.rns.model.Message;
import br.com.rns.model.MessageAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Deserializer;

public class GsonDeserializer<T> implements Deserializer<Message> {


    private final Gson gson = new GsonBuilder().registerTypeAdapter(Message.class, new MessageAdapter()).create();


    @Override
    public Message<T> deserialize(String s, byte[] bytes) {

        return gson.fromJson(new String(bytes), Message.class);
    }
}
