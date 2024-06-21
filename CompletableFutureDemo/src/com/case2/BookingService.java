package com.case2;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

//開發一個服務來預訂酒店與航班
//我們希望這二個任務可以並行
//當任務都都完成後給用戶一個確認
//使用 CompletableFuture 非同步調用來執行任務
public class BookingService {
	
	// 非同步模擬預定酒店的操作
	public CompletableFuture<String> bookHotel() {
		return CompletableFuture.supplyAsync(() -> {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("酒店預定完成");
			return "Hotel booked!";
		});
	}
	
	// 非同步模擬預定航班的操作
	public CompletableFuture<String> bookFlight() {
		return CompletableFuture.supplyAsync(() -> {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("航班預定完成");
			return "Flight booked!";
		});
	}
	
	// 非同步預定酒店與航班
	public void bookTrip() {
		long start = System.currentTimeMillis();
		
		CompletableFuture<String> hotelFuture = bookHotel(); // 開始預定酒店
		CompletableFuture<String> flightFuture = bookFlight(); // 開始預定航班
		
		// 等待二個預定都操作完成
		CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(hotelFuture, flightFuture);
		
		// 當二個操作都完成後執行
		combinedFuture.thenRun(() -> {
			try {
				System.out.println(hotelFuture.get()); // 獲取酒店預定結果
				System.out.println(flightFuture.get()); // 獲取航班預定結果
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			
			long end = System.currentTimeMillis();
			System.out.printf("Total time: %d ms%n", (end - start));
			
		}).join();
		
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		System.out.println("開始預訂");
		
//		BookingService bookingService = new BookingService();
//		bookingService.bookTrip();
		
		
		new Thread(() -> {
			BookingService bookingService = new BookingService();
			Thread bookingThread = new Thread(() -> bookingService.bookTrip());
			bookingThread.start();
			try {
				bookingThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} // 等待 bookingThread 執行完畢
		}).start();
		
		
		System.out.println("做其他事情: 聽音樂");
	}

}
