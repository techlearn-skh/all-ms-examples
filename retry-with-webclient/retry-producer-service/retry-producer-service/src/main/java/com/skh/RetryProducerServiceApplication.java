package com.skh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class RetryProducerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RetryProducerServiceApplication.class, args);
	}

}
