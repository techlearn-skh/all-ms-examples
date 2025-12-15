package com.skh.controllers;


import com.skh.model.Employee;
import com.skh.services.ConsumerServiceImpl;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class ConsumerController {

    @Autowired
    private ConsumerServiceImpl consumerService;

    @Autowired
    private Environment environment;

    @GetMapping(path = "/fetchEmployee/{retryCount}")
    public ResponseEntity<Mono<Employee>> fetchEmployee(@PathVariable Integer retryCount) throws InterruptedException {
        Thread.sleep(3000);
        return new ResponseEntity<>(consumerService.fetchEmployee(retryCount), HttpStatus.OK);
    }

    @GetMapping(path = "/check207")
    public ResponseEntity<Mono<Object>> check207() throws InterruptedException {
        Thread.sleep(3000);
        return new ResponseEntity<>(consumerService.check207(), HttpStatus.OK);
    }

   /* @GetMapping(path = "/fetchAllEmployees")*/
    public ResponseEntity<List<Employee>> fetchAllEmployees() {

        Marker TEST = MarkerFactory.getMarker("TEST");

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/mock")
    public Mono<String> testMockServer() {
        WebClient webClient = WebClient.create("http://localhost:8080");
        return webClient.get()
                .uri("/api/employees/101")
                .retrieve()
                .bodyToMono(String.class);
    }
}







