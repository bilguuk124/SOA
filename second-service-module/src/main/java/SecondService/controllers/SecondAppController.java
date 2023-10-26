package SecondService.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import itmo.MainService.config.LocalDateDeserializer;
import itmo.MainService.config.LocalDateSerializer;
import itmo.MainService.entity.Flat;
import itmo.MainService.exception.IncorrectParametersException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class SecondAppController {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final CompletableFuture<String> responseFuture;
    private final Gson gson;
    private final Logger logger = LogManager.getLogger();

    private static final String REQUEST_TOPIC = "request-topic";
    private static final String RESPONSE_TOPIC = "response-topic";
    private static final String CHEAPER_TOPIC = "cheaper-topic";

    @Autowired
    public SecondAppController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.responseFuture = new CompletableFuture<>();
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                .create();
    }

    @GetMapping("/find-with-balcony/{cheapest}/{with_balcony}")
    public Flat findCheapestOrExpensiveFlatWithOrWithoutBalcony(
            @PathVariable String cheapest,
            @PathVariable String with_balcony
    ) throws IncorrectParametersException, ExecutionException, InterruptedException {
        if (!cheapest.equals("cheap") && !cheapest.equals("expensive")) throw new IncorrectParametersException("Wrong! cheap or expensive?");
        if (!with_balcony.equals("yes") && !with_balcony.equals("no")) throw new IncorrectParametersException("Wrong! With balcony? yes-or-no");
        String serializedMessage = gson.toJson(List.of(cheapest, with_balcony));
        kafkaTemplate.send(REQUEST_TOPIC, serializedMessage);
        kafkaTemplate.flush();
        String response = responseFuture.get();
        return gson.fromJson(response, Flat.class);
    }

    @GetMapping("/get-cheapest/{id1}/{id2}")
    public Flat findCheaperOfTwo(@PathVariable(name = "id1") Integer id1,
                                 @PathVariable(name = "id2") Integer id2) throws IncorrectParametersException, ExecutionException, InterruptedException {
        if (id1 == null || id2 == null) throw new IncorrectParametersException("Id's cannot be null");
        String serializedMessage = gson.toJson(List.of(id1.toString(), id2.toString()));
        logger.info("Sending serialized message: " + serializedMessage);
        kafkaTemplate.send(CHEAPER_TOPIC, serializedMessage);
        kafkaTemplate.flush();
        String response = responseFuture.get();
        logger.info("Got response message: " + response);
        return gson.fromJson(response, Flat.class);
    }

    @KafkaListener(topics = RESPONSE_TOPIC, groupId = "group1")
    public void listenForResponse(String response){
        responseFuture.complete(response);
    }
}
