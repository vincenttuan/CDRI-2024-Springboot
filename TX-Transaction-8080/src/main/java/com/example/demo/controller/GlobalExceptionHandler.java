package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // 處理全域服務
public class GlobalExceptionHandler {
	
	// 處理特定異常
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
		// 建構一個自定義的錯誤物件
		String errorResponse = ex.getMessage();
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}
	
}
