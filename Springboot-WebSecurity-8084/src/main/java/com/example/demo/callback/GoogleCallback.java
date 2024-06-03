package com.example.demo.callback;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.oauth2.OAuth2Util;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/callback/google")
public class GoogleCallback {
	
	@GetMapping
	@ResponseBody
	public String getToken(@RequestParam("code") String code, HttpSession session) throws Exception {
		return code;
	}
	
}
