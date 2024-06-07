package com.example.demo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.model.po.Customer;
import com.example.demo.model.response.ApiResponse;

@FeignClient(name = "FEIGN-CUSTOMER-SERVICE-9092") // 從 Eureka 上查找
public interface CustomerClient {
	@GetMapping("/customers/{id}")
	ApiResponse<Customer> getCustomerById(@PathVariable("id") Integer id);
}
