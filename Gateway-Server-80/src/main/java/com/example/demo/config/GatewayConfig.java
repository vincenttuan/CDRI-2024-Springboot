package com.example.demo.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
	
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("Feign-customer-service-9092", r -> r.path("/customers/**").uri("lb://Feign-customer-service-9092"))
				.route("Feign-product-service-9091", r -> r.path("/products/**").uri("lb://Feign-product-service-9091"))
				.route("Feign-order-service-9093", r -> r.path("/orders/**").uri("lb://Feign-order-service-9093"))
				.build();
	}
	
}
