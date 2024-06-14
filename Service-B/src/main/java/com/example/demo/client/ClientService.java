package com.example.demo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-c")
public interface ClientService {
	
	@GetMapping("/service-c/{name}")
	public String getService(@PathVariable String name);
	
}
