package com.example.demo.producer;

import java.util.Date;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
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
		
		// 非持久化訊息
		// rabbitTemplate.convertAndSend("fanout-exchange", "", data);
		
		
		// 持久化訊息
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
		Message message = new Message(data.getBytes(), messageProperties);
		rabbitTemplate.send("fanout-exchange", "", message);
		
		return "Message: [ " + data + " ] send to Fanout Exchange";
	}
	
}
