package com.example.demo.controller;

import java.util.Date;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ServiceCController {
	
	@GetMapping("/service-c/{name}")
	public String getService(@PathVariable String name, HttpServletRequest request) {
		System.out.println("Inside service-c: " + request.getLocalPort());
		return "Hello " + name + " " + new Date();
	}
	
}
