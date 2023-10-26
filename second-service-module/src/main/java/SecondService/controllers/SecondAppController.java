package SecondService.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import itmo.MainService.entity.Flat;
import itmo.MainService.exception.IncorrectParametersException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class SecondAppController {

    private final KafkaTemplate<String, String> kafkaTemplate;
    CompletableFuture<String> responseFuture;

    private static final String REQUEST_TOPIC = "request-topic";
    private static final String RESPONSE_TOPIC = "response-topic";

    @Autowired
    public SecondAppController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.responseFuture = new CompletableFuture<>();
    }

    @GetMapping("/find-with-balcony/{cheapest}/{with_balcony}")
    public Flat findCheapestOrExpensiveFlatWithOrWithoutBalcony(
            @PathVariable String cheapest,
            @PathVariable String with_balcony
    ) throws IncorrectParametersException, ExecutionException, InterruptedException, JsonProcessingException {
        if (!cheapest.equals("cheap") && !cheapest.equals("expensive")) throw new IncorrectParametersException("Wrong! cheap or expensive?");
        if (!with_balcony.equals("yes") && !with_balcony.equals("no")) throw new IncorrectParametersException("Wrong! With balcony? yes-or-no");
        String serializedMessage = new Gson().toJson(List.of(cheapest, with_balcony));
        kafkaTemplate.send(REQUEST_TOPIC, serializedMessage);
        kafkaTemplate.flush();
        String response = responseFuture.get();
        return new Gson().fromJson(response, Flat.class);
    }

    @KafkaListener(topics = RESPONSE_TOPIC, groupId = "group1")
    public void listenForResponse(String response){
        responseFuture.complete(response);
    }
}
