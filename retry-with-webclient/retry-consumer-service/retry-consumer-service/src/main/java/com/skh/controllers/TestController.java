package com.skh.controllers;

import com.skh.services.TestServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@RestController
public class TestController {

    @Autowired
    private TestServiceImpl testService;
    WebClient webClient = WebClient.create("http://localhost:9001");
    @GetMapping("fetchAllEmployeesTesting")
    void fetchAllEmployees() {
        testService.fetchAllEmployees();

    }

    @GetMapping("fetchAllEmployeesTesting123")
    Mono<String> fetchAllEmployeesTesting() {
        return webClient.get()
                .uri("/fetchAllEmployees")
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(Retry.max(3));
    }

    @GetMapping("fetchAllEmployeesTesting-retry/{id}")
    Mono<String> fetchAllEmployeesretry(@PathVariable Integer id) {
        return webClient.get()
                .uri("/fetchEmployee/{id}", id)
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(Retry.max(3));
    }


}
