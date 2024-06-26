package com.example.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
                                         +---------------+
                                     +-> | fanout-queue1 | --> receiveFanoutQueue1 (consumer)
(producer)     +-----------------+   |   +---------------+
sendFanout --> | fanout-exchange | --+
               +-----------------+   |   +---------------+
                                     +-> | fanout-queue2 | --> receiveFanoutQueue2 (consumer)
                                         +---------------+
*/
@Configuration
public class FanoutExchangeConfig {
	
	@Bean
	public FanoutExchange fanoutExchange() {
		return new FanoutExchange("fanout-exchange");
	}
	
	@Bean
	public Queue fanoutQueue1() {
		return new Queue("fanout-queue1", true); // queue 是否支援持久化保存
	}
	
	@Bean
	public Queue fanoutQueue2() {
		return new Queue("fanout-queue2", true); // queue 是否支援持久化保存
	}
	
	@Bean
	public Binding fanoutBinding1(FanoutExchange fanoutExchange, Queue fanoutQueue1) {
		return BindingBuilder.bind(fanoutQueue1).to(fanoutExchange);
	}
	
	@Bean
	public Binding fanoutBinding2(FanoutExchange fanoutExchange, Queue fanoutQueue2) {
		return BindingBuilder.bind(fanoutQueue2).to(fanoutExchange);
	}
	
}









