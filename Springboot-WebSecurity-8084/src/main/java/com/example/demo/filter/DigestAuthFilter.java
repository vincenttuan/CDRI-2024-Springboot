package com.example.demo.filter;

import java.io.IOException;

import com.example.demo.WebKeyUtil;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 * 
 * 為了實現HTTP摘要驗證，我們需要進一步的邏輯和某些額外的步驟。摘要驗證的主要優勢是它避免明文傳送密碼，而是發送一個摘要。
 * 這是一種摘要算法的結果，通常是MD5，該算法使用密碼、隨機的nonce值、HTTP方法和請求的URI作為輸入。
 * 注意：在實務應用上必須使用 HTTPS 進行通信，以確保傳輸過程中的安全性。
 *
 * 預設的用戶名和密碼，用於HTTP Basic和Digest認證
 * username = user
 * password = 1234
 * */

@WebFilter("/digest_auth/*")
public class DigestAuthFilter implements Filter {
	
	final String REALM = "MY_REPORT";
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		
		// 得到 Authorization 的標頭資訊
		String authHeader = req.getHeader("Authorization");
		
		// 檢查 authHeader 是否是以 'Digest' 開頭並驗證
		if(authHeader != null && 
				authHeader.startsWith("Digest ") && 
				WebKeyUtil.isValidDigest(authHeader, req.getMethod(), req.getRequestURI(), REALM)) {
			// 驗證成功放行
			chain.doFilter(request, response);
		} else {
			// 驗證失敗
			resp.setHeader("WWW-Authenticate", WebKeyUtil.generateDigestChallenge(REALM));
			resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
		}
		
	}
	
}
