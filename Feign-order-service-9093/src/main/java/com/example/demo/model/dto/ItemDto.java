package com.example.demo.model.dto;

import com.example.demo.model.po.Product;

import lombok.Data;

@Data
public class ItemDto {
	private Integer id; // 訂單明細編號
	private Integer quantity; // 訂購數量
	private Integer subtotal; // 小計
	private Product product; // 商品資料
}
