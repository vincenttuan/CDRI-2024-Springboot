package com.example.cart.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
	private Integer id;
	private Integer orderId;
	private ProductDto productDto;
	private Integer amount;
}
