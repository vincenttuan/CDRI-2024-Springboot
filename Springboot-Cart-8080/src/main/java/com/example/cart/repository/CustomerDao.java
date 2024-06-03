package com.example.cart.repository;

import java.util.List;

import com.example.cart.model.po.Customer;

public interface CustomerDao {
	List<Customer> findAllCustomers();
	Customer getCustomerById(Integer id);
	Customer getCustomerByUsername(String username); // 根據 username 查詢
	Customer addCustomer(Customer customer);
	Customer updateCustomer(Customer customer);
	Boolean deleteCustomerById(Integer id);
	// 修改密碼
	Boolean updatePassword(Integer id, String encodedPassword);
}
