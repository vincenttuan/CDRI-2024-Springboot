package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BmiDto {
	private Double height;
	private Double weight;
	private Double bmi;
	private String result;
}
