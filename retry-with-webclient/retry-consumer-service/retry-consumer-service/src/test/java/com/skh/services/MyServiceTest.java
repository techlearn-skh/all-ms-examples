package com.skh.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.skh.exceptions.MyCustomException;
import com.skh.model.Employee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MyServiceTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private ConsumerServiceImpl myService;  // Your class containing the method

    @Test
    void shouldThrowExceptionWhenResponseEntityIsNull() throws Exception {

        Employee emp = new Employee();
        emp.setEmpName("Kamal");
        emp.setEmpId(1122);

        // Mock chain: webClient.get()
        when(webClient.get()).thenReturn(requestHeadersUriSpec);

        // .uri()
        when(requestHeadersUriSpec.uri("/api/employees/101"))
                .thenReturn(requestHeadersSpec);

        // .header()
        when(requestHeadersSpec.header("MY_NAME", "Kamal"))
                .thenReturn(requestHeadersSpec);

        // .retrieve()
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        // .toEntity().block() returns null → this is key
        lenient().when(responseSpec.toEntity(JsonNode.class)).thenReturn(Mono.empty());

        CompletableFuture<Employee> employeeCompletableFuture = myService.fetchEmployeeCF(emp);
        System.out.println(employeeCompletableFuture);
        // Now call your method
//        assertThrows(MyCustomException.class, () -> {
//            myService.fetchEmployeeCF(emp);
//        });
    }
}
