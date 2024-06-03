package com.example.demo.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ship {
	private String name;
	private String type;
	private Integer length;
	private Integer width;
}
