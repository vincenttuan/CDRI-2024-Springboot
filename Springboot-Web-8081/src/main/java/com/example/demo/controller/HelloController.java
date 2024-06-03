package com.example.demo.controller;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // SSR
@RequestMapping("/hello")
public class HelloController {
	
	@GetMapping
	public String hello(Model model) {
		// 將要傳給 jsp 渲染的資料放在 model 中 
		model.addAttribute("name", "Rose");
		model.addAttribute("time", new Date());
		return "hello"; // 會自動指向 /WEB-INF/view/hello.jsp
	}
}
