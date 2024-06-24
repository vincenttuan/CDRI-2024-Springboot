package com.example.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BookDaoImpl implements BookDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public Integer getBookPrice(Integer bookId) {
		String sql = "select book_price from book where book_id=?";
		return jdbcTemplate.queryForObject(sql, Integer.class, bookId);
	}

	@Override
	public Integer getBookStock(Integer bookId) {
		String sql = "select book_amount from stock where book_id=?";
		return jdbcTemplate.queryForObject(sql, Integer.class, bookId);
	}

	@Override
	public Integer getWalletBalance(String username) {
		String sql = "select balance from wallet where username=?";
		return jdbcTemplate.queryForObject(sql, Integer.class, username);
	}

	@Override
	public Integer reduceBookStock(Integer bookId, Integer amountToReduce) {
		// 1. 檢查庫存是否足夠
		Integer bookAmount = getBookStock(bookId);
		if(bookAmount < amountToReduce) { // 庫存不足
			throw new RuntimeException(String.format("book_id:%d 庫存不足 (%d < %d)%n", bookId, bookAmount, reduceBookStock(bookId, amountToReduce)));
		}
		// 2. 更新庫存
		String sql = "update stock set book_amount = book_amount - ? where book_id=?";
		return jdbcTemplate.update(sql, amountToReduce, bookId);
	}

	@Override
	public Integer reduceWalletBalance(String username, Integer bookPrice) {
		// 1. 檢查餘額
		Integer balance = getWalletBalance(username);
		if(balance < bookPrice) { // 餘額不足
			throw new RuntimeException(String.format("username:%s 餘額不足 (%d < %d)%n", username, balance, bookPrice));
		}
		// 2. 更新餘額
		String sql = "update wallet set balance = balance - ? where username = ?";
		return jdbcTemplate.update(sql, bookPrice, username); 
	}

}
