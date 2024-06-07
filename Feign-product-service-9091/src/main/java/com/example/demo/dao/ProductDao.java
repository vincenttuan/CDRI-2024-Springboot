package com.example.demo.dao;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Repository;

import com.example.demo.model.po.Product;

@Repository
public class ProductDao {
	
	// In-Memory
	private static List<Product> products = new CopyOnWriteArrayList<>();
	
	static {
		// 預設的商品資訊
		products.add(new Product(1, "Apple", 10, 5, 100));
		products.add(new Product(2, "Banana", 20, 8, 200));
		products.add(new Product(3, "Cherry", 30, 12, 300));
	}
	
	// 查詢全部商品
	public List<Product> findAll() {
		return products;
	}
	
	// 查詢單筆
	public Optional<Product> findById(Integer id) {
		return products.stream().filter(product -> product.getId().equals(id)).findFirst();
	}
	
	// 商品儲存
	public Product save(Product product) {
		// 建立商品 id: 找到當前最大的商品 id 並加 1
		int maxId = products.stream()
							.mapToInt(Product::getId) // 獲取所有 id
							.max()
							.orElse(0); // 若沒有最大值則返回 0
		
		// 設定商品 id
		product.setId(maxId + 1);
		// 存檔
		products.add(product);
		return product;
	}
	
	// 商品修改
	public Product update(Product product) {
		for(Product p : products) {
			if(p.getId().equals(product.getId())) {
				p.setName(product.getName());
				p.setPrice(product.getPrice());
				p.setCost(product.getCost());
				p.setQuantity(product.getQuantity());
				return p;
			}
		}
		return null;
	}
	
	// 庫存置換
	public Boolean replaceQuantity(Integer id, Integer quantity) {
		for(Product p : products) {
			if(p.getId().equals(id)) {
				p.setQuantity(quantity);
				return true;
			}
		}
		return false;
	}
	
	// 庫存調整
	public Boolean adjustQuantity(Integer id, int amount) {
		for(Product p : products) {
			if(p.getId().equals(id)) {
				// 調整後數量
				int lastQuantity = p.getQuantity() + amount;
				if(lastQuantity < 0) {
					return false;
				}
				p.setQuantity(lastQuantity);
				return true;
			}
		}
		return false;
	}
	
	// 刪除商品
	public Boolean delete(Integer id) {
		Optional<Product> productOpt = findById(id);
		if(productOpt.isEmpty()) {
			return false;
		}
		return products.remove(productOpt.get());
	}
	
}



