package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class JwtServer7001ApplicationTests {

	@Test
	void contextLoads() {
	}
	
	@Test
	void jwtTest() {
		String username = "user";
		String password = "1234";
		
		// 創建 RestTemplate 實例
	    RestTemplate restTemplate = new RestTemplate();

	    // 建立請求的 URL
	    String url = "http://localhost:7001/jwt";

	    // 設定請求的標頭
	    HttpHeaders headers = new HttpHeaders();
	    headers.set("User-Agent", username);
	    headers.set("Service-Identifier", "user");
	    headers.setBasicAuth(username, password); // 設置 Basic Auth

	    // 建立 HttpEntity 物件，包含請求標頭
	    HttpEntity<String> entity = new HttpEntity<>(headers);

	    // 發送請求並獲取響應
	    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

	    // 從響應中提取 JWT
	    String jwt = response.getBody();
	    System.out.println("username: " + username);
	    System.out.println("password: " + password);
	    System.out.println("JWT Token: " + jwt);
	}
}
