package com.assignment.resource;

import com.assignment.model.Requests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("kafka")
public class RequestResource {

    @Autowired
    private KafkaTemplate<String, Requests> kafkaTemplate;

    private static final String TOPIC = "Kafka_Example";

    @GetMapping("/publish/{request1}/{request2}")
    public String post(@PathVariable("request1") final String request1,@PathVariable("request2") final String request2) {

        kafkaTemplate.send(TOPIC, new Requests(request1, request2));

        return "Published successfully";
    }
}
