package com.example.demo.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> { // API 回應
	private Boolean success;  // 是否成功
	private String message;   // 返回訊息
	private T data;           // 返回資料
}
