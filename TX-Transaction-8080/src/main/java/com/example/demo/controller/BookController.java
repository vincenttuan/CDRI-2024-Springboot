package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.BookOneService;

@RestController
@RequestMapping("/book")
public class BookController {
	
	@Autowired
	private BookOneService bookOneService;
	
	// 買單本書
	// Ex: http://localhost:8080/book/one?username=john&bookId=1
	@GetMapping("/one")
	public String buyOneBook(@RequestParam("username") String username, @RequestParam("bookId") Integer bookId) {
		bookOneService.buyOne(username, bookId);
		return String.format("Buy one book OK, username:%s bookId:%d", username, bookId);
	}
	
}
