package com.example.demo.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TopicProducer {
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@GetMapping("/sendTopic")
	// http://localhost:5001/sendTopic/key1.test
	// http://localhost:5001/sendTopic/key1.demo
	// http://localhost:5001/sendTopic/key2.foo.bar
	// http://localhost:5001/sendTopic/key2.demo.name.lab
	// http://localhost:5001/sendTopic/key2.report
	public String sendTopic(@RequestParam String routingKey) {
		String message = "Hello, Topic Exchange";
		rabbitTemplate.convertAndSend("topic-exchange", routingKey, message);
		return "Message [" + message + "] send to Topic Exchange whith routing key: " + routingKey;
	}
	
}
