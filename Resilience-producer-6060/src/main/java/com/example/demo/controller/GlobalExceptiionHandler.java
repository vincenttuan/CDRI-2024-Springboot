package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptiionHandler {
	
	// 處理特定的異常 
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> handleRuntimeException(RuntimeException ex, WebRequest request) {
		
		// 建構一個字定義的錯誤訊息
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
		return new ResponseEntity<>(errorResponse.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	// 自定義一個錯誤回應
	public static class ErrorResponse {
		private int statusCode;
		private String message;

		public ErrorResponse(int statusCode, String message) {
			this.statusCode = statusCode;
			this.message = message;
		}

		public int getStatusCode() {
			return statusCode;
		}

		public void setStatusCode(int statusCode) {
			this.statusCode = statusCode;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}
	
}
