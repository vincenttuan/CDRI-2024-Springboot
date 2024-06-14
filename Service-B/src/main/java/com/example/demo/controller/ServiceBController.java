package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.client.ClientService;

@RestController
public class ServiceBController {
	
	@Autowired
	private ClientService clientService;
	
	@GetMapping("/service-b/{name}")
	public String getService(@PathVariable String name) {
		System.out.println("Inside service-b");
		// 透過 OpenFeign 遠端調用 service-c 的服務
		return clientService.getService(name);
	}
	
}
