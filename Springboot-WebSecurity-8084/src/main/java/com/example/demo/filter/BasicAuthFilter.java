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
 * BasicAuthFilter 是一個實現了 HTTP Basic Authentication 的 Filter。
 * 由於用戶名和密碼在 HTTP 請求中以 Base64 編碼的形式發送（而非加密形式），
 * 注意：在實務應用上必須使用 HTTPS 進行通信，以確保傳輸過程中的安全性。
 * 
 * 預設的用戶名和密碼，用於HTTP Basic和Digest認證
 * username = user
 * password = 1234
 * */

@WebFilter("/basic_auth/*")
public class BasicAuthFilter implements Filter {
	
	final String REALM = "MY_REPORT";
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		
		String authHeader = req.getHeader("Authorization");
		
		if(authHeader != null && WebKeyUtil.isValidBasicAuth(authHeader)) {
			// 驗證通過, 放行
			chain.doFilter(request, response);
		} else {
			resp.setHeader("WWW-Authenticate", WebKeyUtil.generateBasicChallenge(REALM));
			resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
		}
		
	}
	
}







