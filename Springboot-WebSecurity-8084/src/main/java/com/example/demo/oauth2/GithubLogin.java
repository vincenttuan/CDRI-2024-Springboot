package com.example.demo.oauth2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/oauth2/github/login")
public class GithubLogin {
	
	@GetMapping
	public String login() {
		// 建立 Github OAuth 2.0 授權 URL
		String authURL = OAuth2Util.AUTH_URL; 
		return "redirect:" + authURL;
	}
	
}
