package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.po.Customer;
import com.example.demo.model.response.ApiResponse;
import com.example.demo.service.CustomerService;

@RestController
@RequestMapping("/customers") // 根路徑
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	// 查詢所有客戶
	@GetMapping
	public ResponseEntity<ApiResponse<List<Customer>>> findAll() {
		List<Customer> customers = customerService.findAll();
		ApiResponse<List<Customer>> apiResponse = null;
		if(customers.isEmpty()) {
			apiResponse = new ApiResponse<List<Customer>>(false, "查無資料", null);
		} else {
			apiResponse = new ApiResponse<List<Customer>>(true, "有找到資料", customers);
		}
		return ResponseEntity.ok(apiResponse);
	}
	
	// 查詢單一客戶
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<Customer>> findById(@PathVariable Integer id) {
		Customer customer = customerService.findById(id);
		ApiResponse<Customer> apiResponse = null;
		if(customer == null) {
			apiResponse = new ApiResponse<Customer>(false, "查無資料", null);
		} else {
			apiResponse = new ApiResponse<Customer>(true, "有找到資料", customer);
		}
		return ResponseEntity.ok(apiResponse);
	}
	
	// 新增客戶
	@PostMapping // 使用根路徑
	public ResponseEntity<ApiResponse<Customer>> save(@RequestBody Customer customer) {
		Customer savedCustomer = customerService.save(customer);
		ApiResponse<Customer> apiResponse = new ApiResponse(true, "新增客戶成功", savedCustomer);
		return ResponseEntity.ok(apiResponse);
	}
	
	// 修改客戶
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<Customer>> update(@PathVariable Integer id, @RequestBody Customer customer) {
		Customer updatedCustomer = customerService.update(id, customer);
		ApiResponse<Customer> apiResponse = null;
		if(updatedCustomer == null) {
			apiResponse = new ApiResponse(false, "客戶資料修改失敗", customer);
		} else {
			apiResponse = new ApiResponse(true, "客戶資料修改成功", updatedCustomer);
		}
		return ResponseEntity.ok(apiResponse);
	}
	
	// 修改客戶-部分修改
	@PatchMapping("/{id}")
	public ResponseEntity<ApiResponse<Customer>> patch(@PathVariable Integer id, @RequestBody Customer customer) {
		Customer patchCustomer = customerService.findById(id);
		ApiResponse<Customer> apiResponse = null;
		if(patchCustomer == null) {
			apiResponse = new ApiResponse(false, "查無此客戶資料-修改失敗", customer);
		} else {
			// 修改客戶名字
			if(customer.getName() != null) {
				customerService.updateName(id, customer.getName());
			}
			// 修改客戶名字
			if(customer.getEmail() != null) {
				customerService.updateEmail(id, customer.getEmail());
			}
			// 重新查詢修改好的 customer
			Customer newCustomer = customerService.findById(id);
			apiResponse = new ApiResponse(true, "客戶資料修改成功", newCustomer);
		}
		return ResponseEntity.ok(apiResponse);
	}
	
	// 刪除客戶
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
		Boolean status = customerService.delete(id);
		ApiResponse<Void> apiResponse = null;
		if(status) {
			apiResponse = new ApiResponse(true, "客戶資料刪除成功", null);
		} else {
			apiResponse = new ApiResponse(false, "客戶資料刪除失敗", null);
		}
		return ResponseEntity.ok(apiResponse);
	}
	
}
