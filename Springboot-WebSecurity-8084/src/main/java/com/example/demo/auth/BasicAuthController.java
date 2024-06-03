package com.example.demo.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/basic_auth")
public class BasicAuthController {
	
	// 有一個報告連結, 該報告連結需要透過 BasicAuth 才可以觀看
	@GetMapping("/report")
	public String report(@RequestHeader(value = "Authorization", required = false) String authHeader) {
		
		return "Protected Report !";
		
		/*
		if (authHeader != null && WebKeyUtil.isValidBasicAuth(authHeader)) {
			// 驗證成功, 給予受保護的資源
			return ResponseEntity.ok("Protected Report !");
		}
		
		// 驗證失敗/沒有驗證資料, 返回 401 未授權的回應給瀏覽器
		HttpHeaders headers = new HttpHeaders();
		final String REALM = "MY_REPORT";
		headers.set("WWW-Authenticate", WebKeyUtil.generateBasicChallenge(REALM));
		return new ResponseEntity<String>("Unauthorized", headers, HttpStatus.UNAUTHORIZED);
		*/
	}
	
	@GetMapping("/report2")
	public String report2() {
		
		return "Protected Report 2";
	}
	
	@GetMapping("/report3")
	public String report3() {
		
		return "Protected Report 3";
	}
}






