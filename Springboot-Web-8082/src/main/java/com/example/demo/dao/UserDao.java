package com.example.demo.dao;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.po.User;

public interface UserDao {
	List<User> findAllUsers();
	Optional<User> getUser(Integer id);
}
