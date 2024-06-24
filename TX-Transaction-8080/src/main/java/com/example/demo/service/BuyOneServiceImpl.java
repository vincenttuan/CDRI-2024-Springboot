package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.BookDao;

@Service
public class BuyOneServiceImpl implements BookOneService {
	
	@Autowired
	private BookDao bookDao;
	
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void buyOne(String username, Integer bookId) {
		// 1. 查詢價格
		Integer bookPrice = bookDao.getBookPrice(bookId);
		// 2. 減去書本庫存
		bookDao.reduceBookStock(bookId, 1);
		// 3. 修改客戶餘額
		bookDao.reduceWalletBalance(username, bookPrice);
	}
	
}
