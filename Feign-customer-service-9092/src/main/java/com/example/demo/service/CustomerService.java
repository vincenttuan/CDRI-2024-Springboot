package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.CustomerDao;
import com.example.demo.model.po.Customer;

@Service
public class CustomerService {
	@Autowired
	private CustomerDao customerDao;
	
	// 查詢所有客戶
	public List<Customer> findAll() {
		return customerDao.findAll();
	}
	
	// 查詢單一客戶
	public Customer findById(Integer id) {
		return customerDao.findById(id);
	}
	
	// 新增客戶
	public Customer save(Customer customer) {
		return customerDao.save(customer);
	}
	
	// 修改客戶
	public Customer update(Integer id, Customer customer) {
		Customer customerInDb = customerDao.findById(id);
		if (customerInDb == null) {
			return null;
		}
		customer.setId(id);
		return customerDao.update(customer);
	}
	
	// 更新 name
	public void updateName(Integer id, String name) {
		customerDao.updateName(id, name);
	}
	
	// 更新 email
	public void updateEmail(Integer id, String email) {
		customerDao.updateEmail(id, email);
	}
	
	// 刪除客戶
	public boolean delete(Integer id) {
		return customerDao.delete(id);
	}
	
}
