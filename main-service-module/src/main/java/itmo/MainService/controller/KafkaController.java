package itmo.MainService.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import itmo.MainService.config.LocalDateDeserializer;
import itmo.MainService.config.LocalDateSerializer;
import itmo.MainService.entity.Flat;
import itmo.MainService.exception.IncorrectParametersException;
import itmo.MainService.service.FlatService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Controller
public class KafkaController {

    private static final String REQUEST_TOPIC = "request-topic";
    private static final String RESPONSE_TOPIC = "response-topic";
    private static final String CHEAPER_TOPIC = "cheaper-topic";
    private static final Logger logger = LogManager.getLogger();


    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Gson gson;
    private final FlatService flatService;

    @Autowired
    public KafkaController(KafkaTemplate<String, String> kafkaTemplate, FlatService flatService){
        this.kafkaTemplate = kafkaTemplate;
        this.flatService = flatService;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                .create();
    }

    @KafkaListener(topics = REQUEST_TOPIC, groupId = "group1")
    public void consumeKafkaRequest(String message) throws IncorrectParametersException {
        List<String> strings = gson.fromJson(message, List.class);
        String price = strings.get(0);
        String balcony = strings.get(1);
        Flat flat = flatService.getCheapOrExpensiveFlatWithOrWithoutBalcony(price, balcony);
        kafkaTemplate.send(RESPONSE_TOPIC, gson.toJson(flat));
        kafkaTemplate.flush();
    }

    @KafkaListener(topics = CHEAPER_TOPIC, groupId = "group1")
    public void consumeOtherKafkaRequest(String message){
        logger.info(message);
        String[] integers = gson.fromJson(message, String[].class);
        logger.info(Arrays.toString(integers));
        int id1 = Integer.parseInt(integers[0]);
        int id2 = Integer.parseInt(integers[1]);
        Flat flat = flatService.getCheaperOfTwo(id1, id2);
        logger.info("Cheaper is flat with id " + flat.getId());
        kafkaTemplate.send(RESPONSE_TOPIC, gson.toJson(flat));
        kafkaTemplate.flush();
    }
}
