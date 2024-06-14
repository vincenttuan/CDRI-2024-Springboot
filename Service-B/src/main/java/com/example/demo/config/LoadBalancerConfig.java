package com.example.demo.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.loadbalancer.core.RandomLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Feign;

@Configuration
@LoadBalancerClients({
	@LoadBalancerClient(name = "service-c")
	//@LoadBalancerClient(name = "service-c", configuration = RandomLoadBalancerConfig.class)
})
public class LoadBalancerConfig {
	
}

// 隨機策略
class RandomLoadBalancerConfig {
	@Bean
	public RandomLoadBalancer randomLoadBalancer(LoadBalancerClientFactory clientFactory) {
		return new RandomLoadBalancer(clientFactory.getLazyProvider("service-c", ServiceInstanceListSupplier.class), "service-c");
	}
	
	@Bean
	@LoadBalanced
	public Feign.Builder feignBuilder() {
		return Feign.builder();
	}
}


