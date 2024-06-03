package com.example.cart.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.cart.model.dto.ProductDto;
import com.example.cart.model.po.Product;
import com.example.cart.repository.OrderDao;
import com.example.cart.repository.ProductDao;

@Service
public class ProductService {
	@Autowired
	//@Qualifier("InMemoryProduct")
	@Qualifier("InMySQLProduct")
	private ProductDao productDao;
	
	@Autowired
	//@Qualifier("InMemoryOrder")
	@Qualifier("InMySQLOrder")
	private OrderDao orderDao;
	
	@Autowired
	private ModelMapper modelMapper;
	
	public List<ProductDto> queryAllProducts() {
		List<Product> products = productDao.queryAllProducts();
		// PO 轉 DTO
		return products.stream()
				.map(product -> modelMapper.map(product, ProductDto.class))
				.toList();
	}
	
	public ProductDto getProductById(Integer id) {
		if(id == null) {
			return null;
		}
		Product product = productDao.getProductById(id);
		return modelMapper.map(product, ProductDto.class);
	}
	
	public ProductDto addProduct(ProductDto productDto) {
		// 檢查 product 的資料 ...
		// 略...
		// DTO 轉 PO
		Product product = modelMapper.map(productDto, Product.class);
		// 儲存
		Product savedProduct = productDao.addProduct(product);
		// PO 轉 DTO
		return modelMapper.map(savedProduct, ProductDto.class);
	}
	
	public ProductDto updateProduct(Integer id, ProductDto productDto) {
		if(id == null) {
			return null;
		}
		// DTO 轉 PO
		Product product = modelMapper.map(productDto, Product.class);
		// 設定 id
		product.setId(id);
		// 修改 
		Product updatedProduct = productDao.updateProduct(product);
		// PO 轉 DTO
		return modelMapper.map(updatedProduct, ProductDto.class);
	}
	
	public ProductDto updateProductQty(Integer id, Integer qty) { // 修改庫存(庫存覆蓋)
		if(id == null || qty == null || qty < 0) {
			return null;
		}
		Product updatedProduct = productDao.updateProductQty(id, qty);
		return modelMapper.map(updatedProduct, ProductDto.class);
	}
	
	public ProductDto addProductQty(Integer id, Integer increment) { // 增量庫存
		if(id == null || increment == null || increment < 0) {
			return null;
		}
		Product updatedProduct = productDao.addProductQty(id, increment);
		return modelMapper.map(updatedProduct, ProductDto.class);
	}
	
	public ProductDto reduceProductQty(Integer id, Integer decrement) { // 減量庫存
		if(id == null || decrement == null || decrement < 0) {
			return null;
		}
		// 減量後庫存不可 < 0
		Product product = productDao.getProductById(id);
		if(product.getQty() - decrement < 0) {
			return null;
		}
		Product updatedProduct = productDao.reduceProductQty(id, decrement);
		return modelMapper.map(updatedProduct, ProductDto.class);
	}
	
	public Boolean deleteProductById(Integer id) {
		// 若 order item 中有此商品則不可刪除
		if(orderDao.isProductInOrder(id)) {
			return false;
		}
		return id == null ? false : productDao.deleteProductById(id);
	}
}
