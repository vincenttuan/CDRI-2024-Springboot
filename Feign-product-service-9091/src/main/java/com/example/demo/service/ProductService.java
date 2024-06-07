package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.ProductDao;
import com.example.demo.model.po.Product;

@Service
public class ProductService {
	
	@Autowired
	private ProductDao productDao;
	
	// 查詢全部
	public List<Product> findAll() {
		return productDao.findAll();
	}
	
	// 查詢單筆
	public Optional<Product> findById(Integer id) {
		return productDao.findById(id);
	}
	
	// 商品儲存
	public Product save(Product product) {
		return productDao.save(product);
	}
	
	// 商品修改
	public Product update(Integer id, Product product) {
		// 該筆資料是否存在?
		Optional<Product> productInDbOpt = findById(id);
		if(productInDbOpt.isPresent()) {
			product.setId(id); // 設定 id
			return productDao.update(product);
		}
		return null;
	}
	
	// 庫存置換
	public Boolean replaceStock(Integer id, Integer quantity) {
		return productDao.replaceQuantity(id, quantity);
	}
	
	// 庫存調整:增/減
	public Boolean adjustStock(Integer id, Integer amount) {
		return productDao.adjustQuantity(id, amount);
	}
	
	// 刪除商品
	public Boolean delete(Integer id) {
		return productDao.delete(id);
	}
	
	
	
}
