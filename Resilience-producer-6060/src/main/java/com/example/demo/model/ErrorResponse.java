package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//自定義一個錯誤回應
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
	private int statusCode;
	private String message;
}