package com.example.demo.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DirectProducer {
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@GetMapping("/sendDirect")
	public String sendDirect(@RequestParam String routingKey) {
		String message = "Hello, Direct Exchange";
		rabbitTemplate.convertAndSend("direct-exchange", routingKey, message);
		return "Message [" + message + "] send to Direct Exchange with routing key: " + routingKey;
	}
	
	
}
