package com.skh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/fs")
public class FirstController {
    private static final Logger log = LoggerFactory.getLogger(FirstController.class);

    @Autowired
    private WebClient.Builder webClientBuilder;


    @GetMapping("/message")
    public String test() {
        String message = String.format("%s() method called in %s!!!, time %s", Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getName(), LocalDateTime.now());
        log.info(message);
        return message;
    }

    @GetMapping("/call-fs-to-ss")
    public Mono<String> callFromFirstServiceToSecondService() {
        return webClientBuilder.build()
                .get()
                .uri("http://SECOND-SERVICE/ss/message")
                .retrieve()
                .bodyToMono(String.class)
                .doOnSubscribe(sub -> log.info("Calling SECOND-SERVICE"))
                .doOnSuccess(res -> log.info("Response: " + res));
    }

}