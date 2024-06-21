package com.example.demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.Employee;

@SpringBootTest
class RateLimiterTests {
	
	@Test
	void test() throws Exception {
		// 先發送 10 次正常請求
		int i = 1;
		for(;i<=10;i++) {
			System.out.printf("發送第 %d 次 => ", i);
			String responseMessage = getEmployee();
            System.out.println("Response Message: " + responseMessage);
		}
		
		// 第 11 次
		System.out.printf("發送第 %d 次 => ", i);
		String responseMessage = getEmployee();
        System.out.println("Response Message: " + responseMessage);
        
        // 停 1 秒鐘
        System.out.println("停 1 秒鐘");
        Thread.sleep(1000);
        
        i++;
        // 第 12 次
        System.out.printf("發送第 %d 次 => ", i);
		responseMessage = getEmployee();
        System.out.println("Response Message: " + responseMessage);
        
	}
	
	private String getEmployee() throws Exception {
		URL url = new URL("http://localhost:6060/employee/ratelimiter/1");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        StringBuilder sb = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            sb.append(output);
        }
        return sb.toString();
	}
}
