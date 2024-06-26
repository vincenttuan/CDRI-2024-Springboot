package com.example.demo.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class FanoutConsumer {
	
	@RabbitListener(queues = "fanout-queue1")
	public void receiveFanoutQueue1(String message) {
		System.out.println("Received from fanout-queue1: " + message);
	}
	
	@RabbitListener(queues = "fanout-queue2")
	public void receiveFanoutQueue2(String message) {
		System.out.println("Received from fanout-queue2: " + message);
	}
	
}
