package com.example.cart.controller;

import java.security.Principal;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.cart.model.dto.UserDto;
import com.example.cart.model.response.ApiResponse;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@GetMapping("/name")
	public ResponseEntity<ApiResponse<UserDto>> getUsername(Principal principal) {
		UserDto userDto = new UserDto(principal.getName());
		return ResponseEntity.ok(new ApiResponse<>(true, "登入成功", userDto));
	}
		
}
