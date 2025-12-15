package com.skh.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.skh.exceptions.MyCustomException;
import com.skh.exceptions.MyCustomException4001100;
import com.skh.model.Employee;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.SocketPolicy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ConsumerServiceImplTest {
    private MockWebServer mockWebServer;
    private ConsumerServiceImpl consumerService;

    @BeforeEach
    void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start(8080); // method under test calls http://localhost:8080
        consumerService = new ConsumerServiceImpl();
    }

    @AfterEach
    void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    void testMockServer_successReturnsJsonNode() throws Exception {
        // Arrange
        Employee employee = new Employee();
        employee.setEmpId(101);
        employee.setEmpName("Tester");

        String body = "{\"id\":101,\"name\":\"Alice\"}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(body)
                .addHeader("Content-Type", "application/json"));

        // Act
        CompletableFuture<Employee> employeeCompletableFuture = consumerService.fetchEmployeeCF(employee);

         // Assert
        Employee response = employeeCompletableFuture.get();
        assertNotNull(response);
        assertEquals(101, response.getEmpId());
    }

    @Test
    void testMockServer_400WithErrorCode1100_throwsMyCustomException4001100() throws Exception {
        // Arrange
        Employee employee = new Employee();
        employee.setEmpId(101);
        employee.setEmpName("Tester");

        String body = "{\"error\":\"1100\",\"errorDescription\":\"Account error 1100\"}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody(body)
                .addHeader("Content-Type", "application/json"));

        CompletableFuture<Employee> employeeCompletableFuture =
                consumerService.fetchEmployeeCF(employee);
        assertTrue(employeeCompletableFuture.get().getEmpName().contains("Caught MyCustomException4001100"));
    }

    @Test
    void testMockServer_400WithErrorCode1100_throwsMyCustomException4001000() throws Exception {
        // Arrange
        Employee employee = new Employee();
        employee.setEmpId(101);
        employee.setEmpName("Tester");

        String body = "{\"error\":\"1000\",\"errorDescription\":\"Account error 1100\"}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody(body)
                .addHeader("Content-Type", "application/json"));

        CompletableFuture<Employee> employeeCompletableFuture =
                consumerService.fetchEmployeeCF(employee);
        assertTrue(employeeCompletableFuture.get().getEmpName().contains("Caught MyCustomException"));
    }


    @Test
    void testMockServer_throwsException() throws Exception {
        // Arrange
        Employee employee = new Employee();
        employee.setEmpId(2000);
        employee.setEmpName("Tester");

        String body = "{\"id\":101,\"name\":\"Alice\"}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody(body)
                .addHeader("Content-Type", "application/json"));

        CompletableFuture<Employee> employeeCompletableFuture =
                consumerService.fetchEmployeeCF(employee);
        Employee result = employeeCompletableFuture.get(10, TimeUnit.SECONDS);
        assertTrue(result.getEmpName().contains("500 Internal Server Error"));

    }

//    @Test
    void testMockServer_defultValue() throws Exception {
        // Arrange
        Employee employee = new Employee();
        employee.setEmpId(101);
        employee.setEmpName("Tester");

        CompletableFuture<Employee> employeeCompletableFuture =
                consumerService.fetchEmployeeCF(employee);
        Employee result = employeeCompletableFuture.get();
        assertTrue(result.getEmpName().contains("default value..."));

    }

//    @Test
    void testMockServer_responseEntity_NULL() throws Exception {
        // Arrange
        Employee employee = new Employee();
        employee.setEmpId(101);
        employee.setEmpName("Tester");

        mockWebServer.enqueue(new MockResponse()
//                .setResponseCode(200) // No Content - might result in null body
//                .removeHeader("Content-Type")
//                .removeHeader("MY_NAME")
//                .removeHeader("Content-Length")
//                        .setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST)
//                .addHeader("Content-Type", "application/json")
//                .setBody("null")
                        .setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST)
        );


        CompletableFuture<Employee> employeeCompletableFuture =
                consumerService.fetchEmployeeCF(employee);
        Employee result = employeeCompletableFuture.get(10, TimeUnit.SECONDS);
        System.out.println(result);
        assertTrue(result.getEmpName().contains("MyCustomException"));
    }


}
