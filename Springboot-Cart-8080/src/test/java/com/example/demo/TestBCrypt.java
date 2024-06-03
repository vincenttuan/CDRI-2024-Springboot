package com.example.demo;

import java.util.Scanner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestBCrypt {
	
	// 測試 BCrpty 的密碼建立與比對
	public static void main(String[] args) {
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String password1 = "1234";
		String encodedPassword1 = encoder.encode(password1);
		
		String password2 = "1234";
		String encodedPassword2 = encoder.encode(password2);
		
		System.out.printf("明碼: %s  加密後: %s%n", password1, encodedPassword1);
		System.out.printf("明碼: %s  加密後: %s%n", password2, encodedPassword2);
		
		// 比對
		System.out.print("請輸入密碼:");
		String input = new Scanner(System.in).next();
		System.out.println(encoder.matches(input, encodedPassword1));
		System.out.println(encoder.matches(input, encodedPassword2));
	}

}
