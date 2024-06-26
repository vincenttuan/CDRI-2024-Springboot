package com.example.demo.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
                                              key:topic.key1.* 
                                              +--------------+
                                          +-> | topic-queue1 | --> receiveTopicQueue1 (consumer)
(producer)          +-----------------+   |   +--------------+
sendDirect -------> | topic-exchange  | --+   
   |                +-----------------+   |   key:topic.key2.#
   +-> key:topic.key1.test   			  |   +--------------+
   +-> key:topic.key1.demo                +-> | topic-queue2 | --> receiveTopicQueue2 (consumer)
   +-> key:topic.key2.foo.bar                 +--------------+
   +-> key:topic.key2.demo.name.lab
   +-> key:topic.key2.report
*/

@Configuration
public class TopicExchangeConfig {

    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange("topic-exchange");
    }

    @Bean
    Queue topicQueue1() {
        return new Queue("topic-queue1");
    }

    @Bean
    Queue topicQueue2() {
        return new Queue("topic-queue2");
    }

    @Bean
    Binding binding1(TopicExchange topicExchange, Queue topicQueue1) {
        return BindingBuilder.bind(topicQueue1).to(topicExchange).with("topic.key1.*");
    }
    
    // * 可匹配一個單詞, # 可匹配多的單詞
    @Bean
    Binding binding2(TopicExchange topicExchange, Queue topicQueue2) {
        return BindingBuilder.bind(topicQueue2).to(topicExchange).with("topic.key2.#");
    }
}
