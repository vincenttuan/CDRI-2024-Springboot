package com.example.demo.config;

/*
                                         +---------------+
                                     +-> | fanout-queue1 | --> receiveFanoutQueue1 (consumer)
(producer)     +-----------------+   |   +---------------+
sendFanout --> | fanout-exchange | --+
               +-----------------+   |   +---------------+
                                     +-> | fanout-queue2 | --> receiveFanoutQueue2 (consumer)
                                         +---------------+
*/
public class FanoutExchangeConfig {

}
