package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.po.Product;
import com.example.demo.model.response.ApiResponse;
import com.example.demo.service.ProductService;

@RestController
@RequestMapping("/products") // 根路徑
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@GetMapping // 使用根路徑
	public ResponseEntity<ApiResponse<List<Product>>> findAll() {
		List<Product> products = productService.findAll();
		ApiResponse<List<Product>> apiResponse = null;
		if(products.isEmpty()) {
			apiResponse = new ApiResponse<List<Product>>(false, "查無資料", null);
		} else {
			apiResponse = new ApiResponse<List<Product>>(true, "有找到資料", products);
		}
		return ResponseEntity.ok(apiResponse);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<Product>> findById(@PathVariable Integer id) {
		Optional<Product> productOpt = productService.findById(id);
		ApiResponse<Product> apiResponse = null;
		if(productOpt.isEmpty()) {
			apiResponse = new ApiResponse<Product>(false, "查無資料", null);
		} else {
			apiResponse = new ApiResponse<Product>(true, "有找到資料", productOpt.get());
		}
		return ResponseEntity.ok(apiResponse);
	}
	
	@PostMapping // 使用根路徑
	public ResponseEntity<ApiResponse<Product>> save(@RequestBody Product product) {
		Product savedProduct = productService.save(product);
		ApiResponse<Product> apiResponse = new ApiResponse(true, "新增商品成功", savedProduct);
		return ResponseEntity.ok(apiResponse);
	}
	
	@PutMapping("/{id}") // 修改商品
	public ResponseEntity<ApiResponse<Product>> update(@PathVariable Integer id, @RequestBody Product product) {
		Product updatedProduct = productService.update(id, product);
		ApiResponse<Product> apiResponse = null;
		if(updatedProduct == null) {
			apiResponse = new ApiResponse(false, "商品修改失敗", product);
		} else {
			apiResponse = new ApiResponse(true, "商品修改成功", updatedProduct);
		}
		return ResponseEntity.ok(apiResponse);
	}
	
	// Put 修改庫存
	@PutMapping("/{id}/stock/{quantity}")
	public ResponseEntity<ApiResponse<Product>> replaceStock(@PathVariable("id") Integer id, @PathVariable("quantity") Integer quantity) {
		Boolean status = productService.replaceStock(id, quantity);
		ApiResponse<Product> apiResponse = null;
		if(status) {
			Product product = productService.findById(id).get();
			apiResponse = new ApiResponse(true, "庫存修改成功", product);
		} else {
			apiResponse = new ApiResponse(true, "庫存修改失敗", null);
		}
		return ResponseEntity.ok(apiResponse);
	}
	
	// 調整庫存
	@GetMapping("/{id}/stock/{amount}")
	public ResponseEntity<ApiResponse<Product>> adjustStock(@PathVariable("id") Integer id, @PathVariable("amount") Integer amount) {
		Boolean status = productService.adjustStock(id, amount);
		ApiResponse<Product> apiResponse = null;
		if(status) {
			Product product = productService.findById(id).get();
			apiResponse = new ApiResponse(true, "庫存調整成功", product);
		} else {
			apiResponse = new ApiResponse(true, "庫存調整失敗", null);
		}
		return ResponseEntity.ok(apiResponse);
	}
	
	// Delete 刪除商品
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
		Boolean status = productService.delete(id);
		ApiResponse<Void> apiResponse = null;
		if(status) {
			apiResponse = new ApiResponse(true, "商品刪除成功", null);
		} else {
			apiResponse = new ApiResponse(true, "商品刪除失敗", null);
		}
		return ResponseEntity.ok(apiResponse);
	}	
	
}
