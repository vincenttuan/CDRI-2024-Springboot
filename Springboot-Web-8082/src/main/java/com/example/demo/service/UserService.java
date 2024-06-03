package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.UserDao;
import com.example.demo.model.po.User;

@Service
public class UserService {
	
	@Autowired
	private UserDao userDao;
	
	public List<User> findAllUsers() {
		return userDao.findAllUsers();
	}

	public Optional<User> getUser(Integer id) {
		return userDao.getUser(id);
	}
	
}
