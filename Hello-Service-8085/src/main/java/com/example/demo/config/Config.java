package com.example.demo.config;

import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
	
	@Bean
	public InfoContributor appInfoContributor() {
		InfoContributor info = builder -> {
			builder.withDetail("app", "Say Hello & Time");
		};
		
		return info; 
	}
	
}
