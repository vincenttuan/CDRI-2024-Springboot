package com.example.demo.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class TokenStore {
	
	private static Map<String, String> tokens = new ConcurrentHashMap<>();
	
	public String findTokenByServiceId(String serverId) {
		return tokens.get(serverId);
	}
	
	public void storeToken(String serviceId, String token) {
		tokens.put(serviceId, token);
	}
	
}
