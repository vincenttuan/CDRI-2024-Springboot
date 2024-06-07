package com.example.demo.dao;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Repository;

import com.example.demo.model.po.Customer;

@Repository
public class CustomerDao {
	private static List<Customer> customers = new CopyOnWriteArrayList<>();
	static {
		customers.add(new Customer(1, "Alice", "alice@gmail.com"));
		customers.add(new Customer(2, "Bob", "bob@yahoo.com"));
		customers.add(new Customer(3, "Cathy", "cathy@pc.com"));
	}
	
	public List<Customer> findAll() {
		return customers;
	}
	
	public Customer findById(Integer id) {
		for (Customer customer : customers) {
            if (customer.getId().equals(id)) {
                return customer;
            }
        }
        return null;
    }
	
	public Customer save(Customer customer) {
		// 如果產品列表不為空，找到當前最大的ID並加1設置為新產品的ID
	    if (!customers.isEmpty()) {
	        int maxId = customers.stream()
	                            .mapToInt(Customer::getId) // 獲取所有產品的ID
	                            .max() // 找到最大的ID
	                            .getAsInt(); // 獲取最大值
	        customer.setId(maxId + 1); // 將最大ID加1並設置為新產品的ID
	    } else {
	        // 如果產品列表為空，直接將ID設置為1
	    	customer.setId(1);
	    }

	    customers.add(customer); // 添加新產品到列表
	    return customer; // 返回添加的產品
    }
	
	public Customer update(Customer customer) {
		for (Customer c : customers) {
            if (c.getId().equals(customer.getId())) {
                c.setName(customer.getName());
                c.setEmail(customer.getEmail());
                return customer;
            }
        }
		return null;
    }
	
	// 更新 name
	public void updateName(Integer id, String name) {
		for (Customer c : customers) {
            if (c.getId().equals(id)) {
                c.setName(name);
                return;
            }
        }
	}
	
	// 更新 email
	public void updateEmail(Integer id, String email) {
		for (Customer c : customers) {
            if (c.getId().equals(id)) {
                c.setEmail(email);
                return;
            }
        }
	}
	
	public boolean delete(Integer id) {
		Customer customer = findById(id);
		return customers.remove(customer);
	}

}

