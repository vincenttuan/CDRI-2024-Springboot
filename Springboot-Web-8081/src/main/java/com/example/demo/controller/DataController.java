package com.example.demo.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.BmiDto;
import com.example.demo.model.po.Ship;
import com.example.demo.model.response.ApiResponse;

@RestController // CSR
@RequestMapping("/data")
public class DataController {
	
	// 取得今日時間
	@GetMapping("/today")
	public ResponseEntity<ApiResponse<String>> today() {
		Date today = new Date();
		// "yyyy-MM-dd HH:mm:ss E", "yyyy-MM-dd a hh:mm:ss E"
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E");
		String todayString = sdf.format(today);
		ApiResponse<String> apiResponse = new ApiResponse<>(true, "成功", todayString);
		return ResponseEntity.ok(apiResponse);
	}
	
	// 取得樂透幸運數字
	@GetMapping("/lotto")
	public ResponseEntity<ApiResponse<Integer>> lotto() {
		Integer data = new Random().nextInt(100);
		ApiResponse<Integer> apiResponse = new ApiResponse<>(true, "成功", data);
		return ResponseEntity.ok(apiResponse);
	}
	
	// 取得船隻資料
	@GetMapping("/ship")
	public ResponseEntity<ApiResponse<Ship>> getShip() {
		// 模擬船隻資料
		Ship ship = new Ship("Titanic", "郵輪", 280, 30);
		ApiResponse<Ship> apiResponse = new ApiResponse<>(true, "成功", ship);
		return ResponseEntity.ok(apiResponse);
	}
	
	// 取得船隻資料
	@GetMapping("/ship/{id}")
	public ResponseEntity<ApiResponse<Ship>> getShipById(@PathVariable("id") Integer id) {
		// 模擬多艘船隻資料
		List<Ship> ships = List.of(
				new Ship("Titanic", "郵輪", 280, 30),
				new Ship("Evergreen", "貨輪", 350, 40),
				new Ship("順風一號", "客船", 30, 10));
		
		if(id >= ships.size() || id < 0) {
			ApiResponse<Ship> apiResponse = new ApiResponse<>(false, "查無資料", null);
			return ResponseEntity.ok(apiResponse);
		}
		
		Ship ship = ships.get(id);
		ApiResponse<Ship> apiResponse = new ApiResponse<>(true, "成功", ship);
		return ResponseEntity.ok(apiResponse);
	}
	
	// 取得多筆船隻資料
	@GetMapping("/ships")
	public ResponseEntity<ApiResponse<List<Ship>>> getShips() {
		// 模擬多艘船隻資料
		List<Ship> ships = List.of(
				new Ship("Titanic", "郵輪", 280, 30),
				new Ship("Evergreen", "貨輪", 350, 40),
				new Ship("順風一號", "客船", 30, 10));
		ApiResponse<List<Ship>> apiResponse = new ApiResponse<>(true, "成功", ships);
		return ResponseEntity.ok(apiResponse);
	}
	
	// Lab 練習
	// http://localhost:8081/data/bmi?h=170&w=60
	// 可以回應身高,體重,bmi與result
	// bmi <= 18 : 過輕, bmi > 23 : 過重, 其餘正常
	// 提示: 嘗試建立一個 BMI 的 DTO 物件來封裝資料訊息
	//      data.html 中加入一個 BMI 按鈕 + window.prompt 輸入身高與體重
	@GetMapping("/bmi")
	public ResponseEntity<ApiResponse<BmiDto>> bmi(@RequestParam("h") Double h, @RequestParam("w") Double w) {
		Double bmi = w / Math.pow(h/100, 2);
		String result = bmi <= 18 ? "過輕" : bmi > 23 ? "過重" : "正常";
		BmiDto bmiDto = new BmiDto(h, w, bmi, result);
		ApiResponse<BmiDto> apiResponse = new ApiResponse<BmiDto>(true, "成功", bmiDto);
		return ResponseEntity.ok(apiResponse);
	}
	
}







