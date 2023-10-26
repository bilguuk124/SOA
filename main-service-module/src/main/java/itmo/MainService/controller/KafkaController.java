package itmo.MainService.controller;

import com.google.gson.Gson;
import itmo.MainService.entity.Flat;
import itmo.MainService.service.FlatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class KafkaController {

    private static final String REQUEST_TOPIC = "request-topic";
    private static final String RESPONSE_TOPIC = "response-topic";
    private KafkaTemplate<String, String> kafkaTemplate;
    private final Gson gson = new Gson();
    private final FlatService flatService;

    @Autowired
    public KafkaController(KafkaTemplate<String, String> kafkaTemplate, FlatService flatService){
        this.kafkaTemplate = kafkaTemplate;
        this.flatService = flatService;
    }

    @KafkaListener(topics = REQUEST_TOPIC, groupId = "group1")
    public void consumeKafkaRequest(String message){
        List<String> strings = gson.fromJson(message, List.class);
        String price = strings.get(0);
        String balcony = strings.get(1);
        Flat flat = flatService.getCheapOrExpensiveFlatWithOrWithoutBalcony(price, balcony);
        kafkaTemplate.send(RESPONSE_TOPIC, gson.toJson(flat));
    }
}
