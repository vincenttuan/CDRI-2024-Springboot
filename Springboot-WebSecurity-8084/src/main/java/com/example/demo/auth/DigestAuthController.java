package com.example.demo.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/digest_auth")
public class DigestAuthController {
	
	@GetMapping("/report")
	public String report() {
		return "Protected Report !";
	}
	
}
