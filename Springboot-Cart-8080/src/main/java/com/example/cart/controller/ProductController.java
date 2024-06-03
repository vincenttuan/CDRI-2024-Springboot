package com.example.cart.controller;

import java.util.Date;
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

import com.example.cart.model.dto.ProductDto;
import com.example.cart.model.po.Product;
import com.example.cart.model.response.ApiResponse;
import com.example.cart.model.response.StatusMessage;
import com.example.cart.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@GetMapping
	public ResponseEntity<ApiResponse<List<ProductDto>>> queryAllProducts() {
		List<ProductDto> productDtos = productService.queryAllProducts();
		ApiResponse<List<ProductDto>> apiResponse = new ApiResponse<>(true, StatusMessage.成功.name(), productDtos);
		return ResponseEntity.ok(apiResponse);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<ProductDto>> getProduct(@PathVariable("id") Integer id) {
		ProductDto productDto = productService.getProductById(id);
		ApiResponse<ProductDto> apiResponse = null;
		if (productDto == null) {
			apiResponse = new ApiResponse<>(false, StatusMessage.查無資料.name(), null);
		} else {
			apiResponse = new ApiResponse<>(true, StatusMessage.成功.name(), productDto);
		}
		return ResponseEntity.ok(apiResponse);
	}
	
	@PostMapping
	public ResponseEntity<ApiResponse<ProductDto>> addProduct(@RequestBody ProductDto productDto) {
		ProductDto savedProductDto = productService.addProduct(productDto);
		ApiResponse<ProductDto> apiResponse = new ApiResponse<>(true, StatusMessage.新增成功.name(), savedProductDto);
		return ResponseEntity.ok(apiResponse);
	}
	
	@PutMapping("/{productId}")
	public ResponseEntity<ApiResponse<ProductDto>> updateProduct(@PathVariable("productId") Integer productId,
																 @RequestBody ProductDto productDto) {
		ProductDto updatedProductDto = productService.updateProduct(productId, productDto);
		ApiResponse<ProductDto> apiResponse = new ApiResponse<>(true, StatusMessage.修改成功.name(), updatedProductDto);
		return ResponseEntity.ok(apiResponse);
	}
	
	// 部分更新
	// 例如: /1/qty/add
	// 例如: /2/qty/update
	@PatchMapping("/{productId}/qty/{actionName}")
	public ResponseEntity<ApiResponse<ProductDto>> patchProduct(@PathVariable("productId") Integer productId,
																@PathVariable("actionName") String actionName,
			 													@RequestBody ProductDto productDto) {
		ProductDto updateProductDto = null;
		Integer qty = productDto.getQty();
		switch (actionName) {
			case "add":
				updateProductDto = productService.addProductQty(productId, qty);
				break;
			case "update":
				updateProductDto = productService.updateProductQty(productId, qty);
				break;	
			default:
		}
		
		ApiResponse<ProductDto> apiResponse = new ApiResponse<>(true, 
				updateProductDto == null ? StatusMessage.修改數量失敗.name() : StatusMessage.修改數量成功.name(), 
				updateProductDto);
		return ResponseEntity.ok(apiResponse);
	}
	
	@DeleteMapping("/{productId}")
	public ResponseEntity<ApiResponse<Boolean>> deleteProduct(@PathVariable("productId") Integer productId) {
		Boolean status = productService.deleteProductById(productId);
		ApiResponse<Boolean> apiResponse = new ApiResponse<>(status, 
				status ? StatusMessage.刪除成功.name() : StatusMessage.刪除失敗.name(), 
				status);
		return ResponseEntity.ok(apiResponse);
	}
	
	
}
