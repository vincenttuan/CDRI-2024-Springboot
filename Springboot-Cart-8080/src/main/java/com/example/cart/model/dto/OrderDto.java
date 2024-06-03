package com.example.cart.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
	private Integer id; // 訂單 id
	private String date; // 訂單日期
	private Boolean updatable; // 是否可以更新訂單(若日期小於今日則不可以修改)
	private Integer checkout;
	private Integer total; // 訂單總金額
	private CustomerDto customerDto; // 客戶物件
	private List<ItemDto> itemDtos; // 細目列表
}
