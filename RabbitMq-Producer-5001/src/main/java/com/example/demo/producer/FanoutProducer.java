package com.example.demo.producer;

import java.util.Date;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FanoutProducer {
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@GetMapping("/sendFanout")
	public String sendFanout() {
		String data = "Hello Fanout Exchange: " + new Date();
		rabbitTemplate.convertAndSend("fanout-exchange", "", data);
		return "Message: [ " + data + " ] send to Fanout Exchange";
	}
	
}
