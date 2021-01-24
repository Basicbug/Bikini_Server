package com.basicbug.bikini;

import com.basicbug.bikini.util.JwtTokenProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class BikiniApplication {

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public JwtTokenProvider jwtTokenProvider() {
		return new JwtTokenProvider();
	}

	public static void main(String[] args) {
		SpringApplication.run(BikiniApplication.class, args);
	}

}
