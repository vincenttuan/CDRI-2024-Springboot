package com.example.demo.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class DirectConsumer {
	
	@RabbitListener(queues = "direct-queue1")
	public void receiveDirectQueue1(String message) {
		System.out.println("Received from direct-queue1: " + message);
	}
	
	@RabbitListener(queues = "direct-queue2")
	public void receiveDirectQueue2(String message) {
		System.out.println("Received from direct-queue2: " + message);
	}
	
}
