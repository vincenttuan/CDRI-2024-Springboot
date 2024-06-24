package com.example.demo.dao;

public interface BookDao {
	
	Integer getBookPrice(Integer bookId); // 取得書本價格
	Integer getBookStock(Integer bookId); // 取得書本庫存
	Integer getWalletBalance(String username); // 取得目前客戶餘額
	
	Integer reduceBookStock(Integer bookId, Integer amountToReduce); // 更新書本庫存(減量)
	
	Integer reduceWalletBalance(String username, Integer bookPrice); // 更新錢包餘額(減量)
	
}
