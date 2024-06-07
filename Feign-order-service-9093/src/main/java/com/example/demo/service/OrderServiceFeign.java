package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.client.CustomerClient;
import com.example.demo.client.ProductClient;
import com.example.demo.dao.OrderDao;
import com.example.demo.model.dto.ItemDto;
import com.example.demo.model.dto.OrderDto;
import com.example.demo.model.po.Customer;
import com.example.demo.model.po.Item;
import com.example.demo.model.po.Order;
import com.example.demo.model.po.Product;

@Service
public class OrderServiceFeign {
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private CustomerClient customerClient;
	
	@Autowired
	private ProductClient productClient;
	
	// 將 order 從 po 轉 dto
	private OrderDto convertToDto(Order order) {
		OrderDto orderDto = new OrderDto();
		orderDto.setId(order.getId());
		orderDto.setOrderDate(order.getOrderDate());
		
		// 到 http://localhost:9092/customers/1 取得客戶資料
		// 透過 Feign 到 FEIGN-CUSTOMER-SERVICE-9092 下的 /customers/1 取得客戶資料
		Customer customer = customerClient.getCustomerById(order.getCustomerId()).getData();
		orderDto.setCustomer(customer);
		
		return orderDto;
	}
	
	// 將 item 從 po 轉 dto
	private ItemDto convertToDto(Item item) {
		ItemDto itemDto = new ItemDto();
		itemDto.setId(item.getId());
		itemDto.setQuantity(item.getQuantity());
		itemDto.setSubtotal(item.getSubtotal());
		// 透過 Feign 到 FEIGN-PRODUCT-SERVICE-9091 下的 /products/1 取得商品資料
		Product product = productClient.getProductById(item.getProductId()).getData();
		itemDto.setProduct(product);
		return itemDto;
	}
	
	// 查詢單筆訂單
	public OrderDto getOrderById(Integer orderId) {
		Order order = orderDao.getOrderById(orderId);
		// po 轉 dto
		OrderDto orderDto = convertToDto(order);
		return orderDto;
	}
	
}
