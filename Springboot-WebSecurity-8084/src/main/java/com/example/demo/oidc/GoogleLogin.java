package com.example.demo.oidc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/oidc/google/login")
public class GoogleLogin {
	
	@GetMapping
	public String login() {
		String responseType = "code"; // 認證碼變數名稱
		// 範圍（Scope）: 在您的授權請求中，您指定了範圍（scope）為 "email openid"。
        // 這裡的 "openid" 是使用 OpenID Connect 必須的範圍，它表明您的應用程式將使用 OIDC 來對用戶進行身份驗證。
        // 而 "email" 是要求提供用戶的電子郵件資訊。
        // 測試結果: openid 不加也可以（可能是 google oidc 認證系統預設）
		String scope = "email openid";
		
		// 建立 Google 授權 URL
		String authURL = OIDCUtil.getAuthorizationUrl(responseType, scope);
		return "redirect:" + authURL;
	}
	
}
