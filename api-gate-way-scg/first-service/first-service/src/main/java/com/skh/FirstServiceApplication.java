package com.skh;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FirstServiceApplication {



	public static void main(String[] args) {
		SpringApplication.run(FirstServiceApplication.class, args);
	}

	@Configuration
	public class WebClientConfig {
		@Bean
		@LoadBalanced
		public WebClient.Builder webClientBuilder() {
			return WebClient.builder();
		}
	}

}
