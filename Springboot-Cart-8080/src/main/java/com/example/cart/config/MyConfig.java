package com.example.cart.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@Configuration
public class MyConfig {
	// 註冊 ModelMapper
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	// 過濾 http 方法
	// 可以將帶有 _method="PUT" 的 post 表單轉 PUT 送出
	// 可以將帶有 _method="DELETE" 的 post 表單轉 DELETE 送出
	@Bean
	public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
		return new HiddenHttpMethodFilter();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
