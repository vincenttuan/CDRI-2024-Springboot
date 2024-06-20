package com.example.demo;

import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ResilienceProducer6060ApplicationTests {

	@Test
	void contextLoads() {
	}
	
	@Test
	void BulkheadTest() {
		for (int i = 0; i < 200; i++) { // 發送200個並發請求
            new Thread(() -> {
                try {
                    URL url = new URL("http://localhost:6060/employee/semaphone/1");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    int responseCode = conn.getResponseCode();
                    System.out.println("Response Code: " + responseCode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
	}
}
