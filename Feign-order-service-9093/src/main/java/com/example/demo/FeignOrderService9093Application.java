package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
// 設定 Feign Client 位置
@EnableFeignClients(basePackages = "com.example.demo.client")
public class FeignOrderService9093Application {

	public static void main(String[] args) {
		SpringApplication.run(FeignOrderService9093Application.class, args);
	}

}
