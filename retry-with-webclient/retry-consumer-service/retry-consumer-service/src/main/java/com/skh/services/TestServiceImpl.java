package com.skh.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.skh.exceptions.MyCustomException;
import com.skh.exceptions.MyCustomException4001100;
import com.skh.model.Employee;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class TestServiceImpl {
    WebClient webClient = WebClient.create("http://localhost:9001");

    public void fetchAllEmployees() {

        System.out.println(fetchSingleEmplyee());

//        webClient.get()
//                .uri("/api/employees")
//                .retrieve()
//                .bodyToFlux(Employee.class)
//                .subscribe(
//                        employee -> System.out.println("Received: " + employee),
//                        error -> System.err.println("Error: " + error.getMessage())
//                );

    }

    public Mono<List<Employee>> fetchSingleEmplyee() {
        return webClient.get()
                .uri("/employees")
                .retrieve()
                .bodyToFlux(Employee.class)
                .collectList();
    }


}
