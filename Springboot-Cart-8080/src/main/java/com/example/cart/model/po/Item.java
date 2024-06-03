package com.example.cart.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
	private Integer id;
	private Integer orderId;
	private Integer productId;
	private Integer amount;
}
