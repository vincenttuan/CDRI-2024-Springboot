package com.example.demo.controller;

import java.security.Principal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.JwtServiceNimbus;
import com.nimbusds.jose.JOSEException;

import jakarta.servlet.http.HttpServletRequest;

//Access-Control-Allow-Origin
@CrossOrigin
@RestController
public class JWTController {
	
	@Autowired
    private JwtServiceNimbus jwtServiceNimbus;
	
	private static Map<String, String> tokens = new ConcurrentHashMap<>();
	
	@GetMapping("/")
	public String index() {
        return "This is a JWT Service. Please use /jwt to get a token, and use /verifyJwt to verify";
	}
	
	@GetMapping("/jwt")
    public ResponseEntity<String> getJWT(
    		@RequestHeader("User-Agent") String userAgent,
    		@RequestHeader("Service-Identifier") String serviceId,
    		@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) throws JOSEException {
        
		// 打印接收到的请求头信息
        System.out.println("User-Agent: " + userAgent);
        System.out.println("Service-Identifier: " + serviceId);
        System.out.println("Authorization: " + authorizationHeader);

        // 检查Basic Auth
        if (!authorizationHeader.startsWith("Basic ")) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        
        // 解码Basic Auth
        String base64Credentials = authorizationHeader.substring("Basic ".length());
        byte[] decodedBytes = java.util.Base64.getDecoder().decode(base64Credentials);
        String decodedCredentials = new String(decodedBytes);
        String[] credentials = decodedCredentials.split(":", 2);
        String username = credentials[0];
        String password = credentials[1];
        System.out.println("username: " + username);
        System.out.println("password: " + password);
        
        // 检查用户名和密码
		if (!"user".equals(username) || !"1234".equals(password)) {
			return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
		}
        
        String storedJwt = tokens.get(username); // 取得該使用者的 JWT Token
        System.out.println("storedJwt: " + storedJwt);
        
		if (storedJwt != null) {
			System.out.println("找到已存在的 JWT Token: " + storedJwt);
			return ResponseEntity.ok(storedJwt);
		}
		// 根據使用者名稱產生一個新的 JWT Token, 有效期限 3600000 毫秒
		String newJwt = jwtServiceNimbus.createToken(serviceId, username, 3600000);
		System.out.println("產生一個新的 JWT Token: " + newJwt);
		tokens.put(username, newJwt); // 將新的該使用者的 JWTToken 存入 Map
		return ResponseEntity.ok(newJwt);
    }
	
	@GetMapping("/verifyJwt")
    public ResponseEntity<String> verifyJwt(@RequestParam String token, HttpServletRequest request) {
		// 來自ip
		String ip = request.getRemoteAddr();
		int port = request.getRemotePort();
		System.out.print("來自：" + ip + ":" + port + " 的請求，");
        // 驗證 JWT 是否有效
        if (!jwtServiceNimbus.verifyToken(token)) {
            System.out.println("JWT 是無效的。");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT 是無效的。");
        }
        
        String username = jwtServiceNimbus.getJWTClaims(token).get("username").toString();

        // 檢查用戶名是否存在於 tokens Map 中
        if (!tokens.containsKey(username)) {
            System.out.println("JWT 是有效的。但用戶名不存在於 tokens Map 中。");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT 是有效的。但用戶名不存在於 tokens Map 中。");
        }

        // 檢查 token 是否與 tokens Map 中的 token 相同
        if (!token.equals(tokens.get(username))) {
            System.out.println("JWT 是有效的。但 token 與 tokens Map 中的 token 不相同。");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT 是有效的。但 token 與 tokens Map 中的 token 不相同。");
        }

        System.out.println("JWT 是有效的。Token 與 tokens Map 中的 token 相同。");
        return ResponseEntity.ok("JWT 是有效的。");
    }
    
    @GetMapping("/getJWTClaims")
	public ResponseEntity<Map<String, Object>> getJWTClaims(@RequestParam String token) {
		Map<String, Object> claims = jwtServiceNimbus.getJWTClaims(token);
		if (claims != null) {
			return ResponseEntity.ok(claims);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}

	@GetMapping("/clear")
	public ResponseEntity<String> clear(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
		// 從 Authorization Header jwt 取得 payload username
		String token = authorizationHeader.substring("Bearer ".length());
		String username = jwtServiceNimbus.getJWTClaims(token).get("username").toString();
		System.out.println("clear username: " + username);
		if (tokens.remove(username) == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(username + "'s token is not found.");
		}
		return ResponseEntity.ok(username + "'s token is cleared.");
	}

	
}