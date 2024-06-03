package com.example.cart.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.cart.model.dto.CustomerDto;
import com.example.cart.model.dto.OrderDto;
import com.example.cart.model.po.Item;
import com.example.cart.model.dto.ItemDto;
import com.example.cart.model.response.ApiResponse;
import com.example.cart.repository.OrderDao;
import com.example.cart.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@GetMapping
	public ResponseEntity<ApiResponse<List<OrderDto>>> getAllOrders() {
		List<OrderDto> orderDtos = orderService.getAllOrders();
		ApiResponse<List<OrderDto>> apiResponse = new ApiResponse<>(true, "Success", orderDtos);
		return ResponseEntity.ok(apiResponse);
	}
	
	@GetMapping("/checkout/{orderId}")
	public ResponseEntity<ApiResponse<Boolean>> checkoutOrderById(@PathVariable("orderId") Integer orderId) {
		Boolean checkoutStatus = orderService.checkoutOrderById(orderId);
		ApiResponse<Boolean> apiResponse = new ApiResponse<>(true, "Success", checkoutStatus);
		return ResponseEntity.ok(apiResponse);
	}
	
	@GetMapping("/{orderId}")
	public ResponseEntity<ApiResponse<OrderDto>> getOrderById(@PathVariable("orderId") Integer orderId) {
		OrderDto orderDto = orderService.getOrderById(orderId);
		ApiResponse<OrderDto> apiResponse = new ApiResponse<>(true, "Success", orderDto);
		return ResponseEntity.ok(apiResponse);
	}
	
	@GetMapping("/customer/{customerId}")
	public ResponseEntity<ApiResponse<List<OrderDto>>> getOrdersByCustomerId(
			@PathVariable("customerId") Integer customerId, Principal principal) {
		List<OrderDto> orderDtos = orderService.getOrdersByUsername(principal.getName());
		ApiResponse<List<OrderDto>> apiResponse = new ApiResponse<>(true, "Success", orderDtos);
		return ResponseEntity.ok(apiResponse);
	}
	
	@GetMapping("/customer/uncheckout")
	public ResponseEntity<ApiResponse<List<OrderDto>>> getUncheckoutOrdersByCustomerId(Principal principal) {
		List<OrderDto> orderDtos = orderService.getUncheckoutOrdersByUsername(principal.getName());
		ApiResponse<List<OrderDto>> apiResponse = new ApiResponse<>(true, "Success", orderDtos);
		return ResponseEntity.ok(apiResponse);
	}
	
	@GetMapping("/customer/checkouted")
	public ResponseEntity<ApiResponse<List<OrderDto>>> getCheckoutedOrdersByCustomerId(Principal principal) {
		List<OrderDto> orderDtos = orderService.getCheckoutedOrdersByUsername(principal.getName());
		ApiResponse<List<OrderDto>> apiResponse = new ApiResponse<>(true, "Success", orderDtos);
		return ResponseEntity.ok(apiResponse);
	}
	
	@PostMapping
	public ResponseEntity<ApiResponse<OrderDto>> addOrReduceOrderItem(@RequestBody Item item, Principal principal) {
		// 透過 principal.getName() 取得當前登入的使用者名稱並新增訂單
		OrderDto orderDto = orderService.addOrder(principal.getName());
		// 透過 orderDto.getId() 取得訂單編號並新增訂單項目
		orderService.addOrReduceOrderItem(orderDto.getId(), item.getProductId(), item.getAmount());
		// 更新 orderDto
		orderDto = orderService.getOrderById(orderDto.getId());
		ApiResponse<OrderDto> apiResponse = new ApiResponse<>(true, "Success", orderDto);
		return ResponseEntity.ok(apiResponse);
	}
	
	@DeleteMapping("/{orderId}")
	public ResponseEntity<ApiResponse<Boolean>> deleteOrder(@PathVariable Integer orderId) {
		boolean status = orderService.deleteOrder(orderId);
		ApiResponse<Boolean> apiResponse = new ApiResponse<>(true, "Success", status);
		return ResponseEntity.ok(apiResponse);
	}
	
	@DeleteMapping("/item/{itemId}")
	public ResponseEntity<ApiResponse<Boolean>> deleteOrderItem(@PathVariable Integer itemId) {
		boolean status = orderService.deleteOrderItem(itemId);
		ApiResponse<Boolean> apiResponse = new ApiResponse<>(true, "Success", status);
		return ResponseEntity.ok(apiResponse);
	}
	
}
