package com.example.cart.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
	private Integer id;    // 商品 id
	private String name;   // 商品名稱
	private Integer cost;  // 商品成本
	private Integer price; // 商品售價
	private Integer qty;   // 商品數量
}
