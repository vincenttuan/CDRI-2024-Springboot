package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.client.CustomerClient;
import com.example.demo.dao.OrderDao;
import com.example.demo.model.dto.OrderDto;
import com.example.demo.model.po.Customer;
import com.example.demo.model.po.Order;

@Service
public class OrderServiceFeign {
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private CustomerClient customerClient;
	
	// 將 order 從 po 轉 dto
	private OrderDto convertToDto(Order order) {
		OrderDto orderDto = new OrderDto();
		orderDto.setId(order.getId());
		orderDto.setOrderDate(order.getOrderDate());
		
		// 到 http://localhost:9092/customers/1 取得客戶資料
		// 到 FEIGN-CUSTOMER-SERVICE-9092 下的 /customers/1 取得客戶資料
		Customer customer = customerClient.getCustomerById(order.getCustomerId()).getData();
		orderDto.setCustomer(customer);
		
		return orderDto;
	}
	
	// 查詢單筆訂單
	public OrderDto getOrderById(Integer orderId) {
		Order order = orderDao.getOrderById(orderId);
		// po 轉 dto
		OrderDto orderDto = convertToDto(order);
		return orderDto;
	}
	
}
