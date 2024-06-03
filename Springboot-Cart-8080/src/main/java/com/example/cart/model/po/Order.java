package com.example.cart.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
	private Integer id; // 訂單 id
	private String date; // 訂單日期
	private Integer checkout; // 結帳狀態
	private Integer customerId; // 客戶編號
}
