package com.skh.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.skh.exceptions.MyCustomException;
import com.skh.exceptions.MyCustomException4001100;
import com.skh.model.Employee;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import javax.crypto.spec.PSource;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class ConsumerServiceImpl {
    WebClient webClient = WebClient.create("http://localhost:8080");
    public Mono<Employee> fetchEmployee(Integer retryCount) throws InterruptedException {
        return WebClient.create("http://localhost:9001")
                .get()
                .uri("/fetchEmployee/"+retryCount)
                .retrieve()
                .onRawStatus(statusCode -> statusCode == 500, response -> response.bodyToMono(JsonNode.class)
                        .handle((error, sink) -> sink.error(new NullPointerException(error.toString()+" onRawStatus"))))
                .bodyToMono(Employee.class)
                .flatMap(this::validateResponse)
                .retryWhen(
                        Retry.backoff(1, Duration.ofSeconds(2)) // 3 retries, 2 sec delay
                                .filter(this::checkExceptionType)
                                .doBeforeRetry(retrySignal -> System.out.println(String.format("totalRetriesInARow: %d, time: %s",(retrySignal.totalRetriesInARow() + 1), LocalDateTime.now())))
                                .onRetryExhaustedThrow((retryBackoffSpec,retrySignal) -> {
                                    throw new NullPointerException(String.format("*** Retry Exhausted ***..!!  %s %s", retryBackoffSpec, retrySignal));
                                })
                )
                .onErrorResume(NullPointerException.class, err -> Mono.error(new NullPointerException("OnErroResume() method")))
                .doOnError(NullPointerException.class, err -> System.out.println("doOnError() method"));
    }

    boolean checkExceptionType(Throwable throwable){
        System.out.println("EX class name : "+throwable.getClass().getName());
        System.out.println("Inside filter, Exception Matched? - " + (throwable instanceof NullPointerException ||
                throwable instanceof FileNotFoundException));
        return throwable instanceof NullPointerException ||
//                throwable instanceof ArithmeticException ||
                throwable instanceof FileNotFoundException;
    }

    private Mono<Employee> validateResponse(Employee employeeResponse){

      //  employeeResponse = new Random().nextInt() % 2 == 0 ? employeeResponse = null : employeeResponse;
        if(null == employeeResponse){
            return Mono.error(new MyCustomException("Response is NULL"));
        }
        return Mono.just(employeeResponse);
    }



    public Mono<Object> check207() {
        return WebClient.create("http://localhost:9001")
                .get()
                .uri("/check207")
                .retrieve()
                .onRawStatus(statusCode -> statusCode == 500, response -> response.bodyToMono(JsonNode.class)
                        .handle((error, sink) -> sink.error(new NullPointerException(error.toString()+" onRawStatus"))))
                .onRawStatus(statusCode -> statusCode == 207, response -> response.bodyToMono(JsonNode.class)
                        .handle((error, sink) -> sink.error(new NullPointerException(error.toString()+" 207-Multi-Status"))))
                .bodyToMono(Object.class)
//                .flatMap(this::validateResponse)
                .retryWhen(
                        Retry.backoff(3, Duration.ofSeconds(2)) // 3 retries, 2 sec delay
                                .filter(this::checkExceptionType)
                                .doBeforeRetry(retrySignal -> System.out.println(String.format("totalRetriesInARow: %d, time: %s",(retrySignal.totalRetriesInARow() + 1), LocalDateTime.now())))
                                .onRetryExhaustedThrow((retryBackoffSpec,retrySignal) -> {
                                    throw new NullPointerException(String.format("Retry Exhausted..!!  %s %s", retryBackoffSpec, retrySignal));
                                })
                );
    }

    private ResponseEntity<JsonNode> testWebMockServer(Employee employee) throws Exception {

        ResponseEntity<JsonNode> responseEntity = webClient.get()
                .uri("/api/employees/101")
                .header("MY_NAME", employee.getEmpName())
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode.value() == 400,
                        clientResponse -> clientResponse.bodyToMono(JsonNode.class)
                                .flatMap(this::process400Errors))
                .toEntity(JsonNode.class)
                .block();
        return Optional.ofNullable(responseEntity).orElseThrow(() -> new MyCustomException("NULLLLLLLLL"));

    }

    private Mono<? extends Throwable> process400Errors(JsonNode jsonNode) {
        String errorCode = jsonNode.path("error").asText();
        if("1100".equals(errorCode)){
            return Mono.error(new MyCustomException4001100("Custom EX for error code 1100"));
        } else {
            return Mono.error(new MyCustomException("Generic EX for other error codes"));

        }
    }

    public CompletableFuture<Employee> fetchEmployeeCF(Employee employee) throws Exception {

        StringBuilder logBuilder = new StringBuilder();

        if(employee.getEmpId() == 1211){
            employee.setEmpName("default value...");
            return CompletableFuture.completedFuture(employee);
        }

        try {
            ResponseEntity<JsonNode> responseEntity = testWebMockServer(employee);
            if(responseEntity.hasBody()){
                System.out.println("Response Body: "+responseEntity.getBody().toString());
                logBuilder.append("Response Body: ").append(responseEntity.getBody().toString()).append("\n");
                employee.setEmpName("Success");

            }

        }catch (MyCustomException4001100 exception4001100){
            logBuilder.append("Caught MyCustomException4001100: ").append(exception4001100.getMessage()).append("\n");
            employee.setEmpName("MyCustomException4001100 called");
        }catch (MyCustomException myCustomException) {
            logBuilder.append("Caught MyCustomException: ").append(myCustomException.getMessage()).append("\n");
            employee.setEmpName("MyCustomException called");
        }
        catch (Exception exception) {
            logBuilder.append("Caught Exception: ").append(exception.getMessage()).append("\n");
            employee.setEmpName("Exception called");
            System.out.println("mmm : "+exception.getMessage());
        }

        employee.setEmpName(logBuilder.toString());

        return CompletableFuture.completedFuture(employee);
    }



}
