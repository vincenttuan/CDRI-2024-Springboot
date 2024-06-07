package com.example.demo.model.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.example.demo.model.po.Customer;

import lombok.Data;

@Data
public class OrderDto {
	private Integer id; // 訂單編號
	private String orderDate; // 訂單日期
	private Customer customer; // 客戶資料
	private List<ItemDto> itemDtos = new CopyOnWriteArrayList<>(); // 訂單細目
}
