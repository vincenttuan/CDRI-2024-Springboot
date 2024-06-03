package com.example.demo.filter;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import com.example.demo.WebKeyUtil;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter("/form_auth/*")
public class LoginFilter implements Filter {
	
	static Map<String, Map<String, String>> users = new HashMap<>();
	
	static {
		users.put("user", Map.of("hash", "2727b785247f2f9106e2919322aef99020f75d57a70ae50a968d69322904b7cc", "salt", "42c258612fb2456bf61dae6c75d47097")); // 原始密碼: 1234
		users.put("john", Map.of("hash", "f1ed6eecb89f86b0d5c823afdfd7f0dafaa7ab296336a0df5c4149b9664b1525", "salt", "79747ce06931f9d06c4eaffe38c5a662")); // 原始密碼: 5678
		users.put("mary", Map.of("hash", "d5b5475f566c367c803120eb3faf931c05574b2535c89bca1dc7d75421738ace", "salt", "b273d17d9c6fd4c6afeec12e15698f30")); // 原始密碼: 8888
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		HttpSession session = req.getSession();
		// 判斷使用者是否已經登入 ?
		Object loginStatue = session.getAttribute("loginStatue");
		if(loginStatue != null && Boolean.valueOf(loginStatue+"")) {
			// 放行
			chain.doFilter(request, response);
		} else {
			// 驗證 username & password
			String username = req.getParameter("username");
			String password = req.getParameter("password");
			// 判斷是否有輸入 username
			if(username == null || username.trim().length() == 0) {
				resp.sendRedirect("/login");
				return;
			}
			
			// 判斷 csrfToken 令牌是否有效 ?
			String csrfToken = req.getParameter("csrfToken"); // 表單傳來的 token
			String csrfTokenFromSession = session.getAttribute("csrfToken") + "";
			
			if(!csrfTokenFromSession.equals(csrfToken)) {
				System.out.println("CSRF Token 錯誤");
				resp.sendRedirect("/login");
				return;
			}
			
			// 是否有此 username ?
			Map<String, String> user = users.get(username);
			if(user == null) {
				System.out.println("無此使用者");
				resp.sendRedirect("/login");
				return;
			}
			// 判斷 password
			// 得到使用者的 hash 與 salt
			String hash = user.get("hash"); // 使用者的 hash
			byte[] salt = WebKeyUtil.hexStringToByteArray(user.get("salt")); // 使用者的 salt
			
			try {
				MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
				messageDigest.reset(); // 重制
				messageDigest.update(salt); // 加鹽
				byte[] inputHashedBytes = messageDigest.digest(password.getBytes());
				// 根據使用者輸入的 password 與已知的 salt 來產出 comparedHash
				String comparedHash = WebKeyUtil.bytesToHexString(inputHashedBytes);
				// 比較 comparedHash(運算的) 與 hash(已儲存的) 是否相等
				if(comparedHash.equals(hash)) {
					// 儲存登入狀態
					session.setAttribute("loginStatue", true);
					// 放行
					chain.doFilter(request, response);
					return;
				} else {
					System.out.println("登入失敗");
					resp.sendRedirect("/login");
					return;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				resp.sendRedirect("/login");
			}
			
		}
		
		
	}
	
}
