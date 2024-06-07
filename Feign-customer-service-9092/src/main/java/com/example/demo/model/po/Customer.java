package com.example.demo.model.po;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer { // 客戶
	private Integer id; // 客戶編號
	private String name; // 客戶名稱
	private String email; // 客戶 email

}
