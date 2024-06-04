package com.example.demo.config;

import java.util.Map;

import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
	
	@Bean
	public InfoContributor appInfoContributor() {
		InfoContributor info = builder -> {
			//builder.withDetail("app", "Say Hello & Time");
			builder.withDetail("app", 
					Map.of(
							"name", "Hello App",
							"description", "Say Hello & Time",
							"version", "1.0.0",
							"author", "John",
							"last_updated", "2024-06-04"
					));
		};
		
		return info; 
	}
	
}
