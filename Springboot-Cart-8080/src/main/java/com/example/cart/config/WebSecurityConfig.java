package com.example.cart.config;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.example.cart.service.CustomerService;

@EnableWebSecurity // 啟用 Spring Security
@Configuration // 這是一個配置類
public class WebSecurityConfig {
	
	@Autowired
	private CustomerService customerService;
	
	// 安全過濾配置
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((authz) -> authz
				.requestMatchers("/css/**", "/js/**", "/image/**").permitAll()
				//.requestMatchers("/customers/**").hasRole("ADMIN")
				.requestMatchers("/customers/**").hasAnyRole("USER", "ADMIN")
				.requestMatchers("/products/**").hasAnyRole("USER", "ADMIN")
				.anyRequest().authenticated())
				.httpBasic(Customizer.withDefaults()) // 啟用 HTTP 基本驗證, 給 Rest 工具測試使用(例如: Insomnia, Postman) 
				.formLogin(Customizer.withDefaults())  // 預設表單登入頁面, 給網頁用
				.csrf(csrf -> csrf.disable()) // 關閉 CSRF(跨站請求偽造) 保護 
				.cors(Customizer.withDefaults()); // 預設: 啟用 CORS (跨域資源共享)  
		
		return http.build(); // 建立安全過濾器鏈
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customerService);
	}
	
}
