package com.example.demo.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 		                                 key:routingKey1 
		                                 +---------------+
		                             +-> | direct-queue1 | --> receiveDirectQueue1 (consumer)
(producer)     +-----------------+   |   +---------------+
sendDirect --> | direct-exchange | --+   
   |           +-----------------+   |   key:routingKey2
   +-> key:routingKey1      	     |   +---------------+
   +-> key:routingKey2               +-> | direct-queue2 | --> receiveDirectQueue2 (consumer)
		                                 +---------------+

*/


@Configuration
public class DirectExchangeConfig {

    @Bean
    DirectExchange directExchange() {
        return new DirectExchange("direct-exchange");
    }

    @Bean
    Queue directQueue1() {
        return new Queue("direct-queue1");
    }

    @Bean
    Queue directQueue2() {
        return new Queue("direct-queue2");
    }

    @Bean
    Binding directBinding1(DirectExchange directExchange, Queue directQueue1) {
        return BindingBuilder.bind(directQueue1).to(directExchange).with("routingKey1");
    }

    @Bean
    Binding directBinding2(DirectExchange directExchange, Queue directQueue2) {
        return BindingBuilder.bind(directQueue2).to(directExchange).with("routingKey2");
    }
}
