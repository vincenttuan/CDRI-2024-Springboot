package com.example.cart.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.cart.model.dto.CustomerDto;
import com.example.cart.model.dto.ProductDto;
import com.example.cart.model.response.ApiResponse;
import com.example.cart.model.response.StatusMessage;
import com.example.cart.service.CustomerService;

@Controller
@RequestMapping("/customers")
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	@GetMapping
	public ResponseEntity<ApiResponse<List<CustomerDto>>> queryAllCustomers() {
		List<CustomerDto> customerDtos = customerService.findAllCustomers();
		ApiResponse<List<CustomerDto>> apiResponse = new ApiResponse<>(true, StatusMessage.成功.name(), customerDtos);
		return ResponseEntity.ok(apiResponse);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<CustomerDto>> getCustomer(@PathVariable("id") Integer id) {
		CustomerDto customerDto = customerService.getCustomerById(id);
		ApiResponse<CustomerDto> apiResponse = null;
		if (customerDto == null) {
			apiResponse = new ApiResponse<>(false, StatusMessage.查無資料.name(), null);
		} else {
			apiResponse = new ApiResponse<>(true, StatusMessage.成功.name(), customerDto);
		}
		return ResponseEntity.ok(apiResponse);
	}
	
	@PostMapping
	public ResponseEntity<ApiResponse<CustomerDto>> addProduct(@RequestBody CustomerDto customerDto) {
		CustomerDto savedCustomerDto = customerService.addCustomer(customerDto);
		ApiResponse<CustomerDto> apiResponse = new ApiResponse<>(true, StatusMessage.新增成功.name(), savedCustomerDto);
		return ResponseEntity.ok(apiResponse);
	}
	
	@PutMapping("/{customerId}")
	public ResponseEntity<ApiResponse<CustomerDto>> updateProduct(@PathVariable("customerId") Integer customerId,
																 @RequestBody CustomerDto customerDto) {
		CustomerDto updatedCustomerDto = customerService.updateCustomer(customerId, customerDto);
		ApiResponse<CustomerDto> apiResponse = new ApiResponse<>(true, StatusMessage.修改成功.name(), updatedCustomerDto);
		return ResponseEntity.ok(apiResponse);
	}
	
	@DeleteMapping("/{customerId}")
	public ResponseEntity<ApiResponse<Boolean>> deleteProduct(@PathVariable("customerId") Integer customerId) {
		Boolean status = customerService.deleteCustomerById(customerId);
		ApiResponse<Boolean> apiResponse = new ApiResponse<>(status, 
				status ? StatusMessage.刪除成功.name() : StatusMessage.刪除失敗.name(), 
				status);
		return ResponseEntity.ok(apiResponse);
	}
	
	@PatchMapping("/{customerId}/update-password")
	public ResponseEntity<ApiResponse<Boolean>> updatePassword(@PathVariable("customerId") Integer customerId, @RequestBody CustomerDto customerDto) {
		String newPassword = customerDto.getPassword();
		Boolean status = customerService.updatePassword(customerId, newPassword);
		ApiResponse<Boolean> apiResponse = new ApiResponse<>(status, 
				status ? StatusMessage.修改成功.name() : StatusMessage.修改失敗.name(), 
				status);
		return ResponseEntity.ok(apiResponse);
	}
	
}
