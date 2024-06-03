package com.example.demo.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.model.po.User;

@Repository
public class UserDaoImpl implements UserDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<User> findAllUsers() {
		String sql = "select id, name from user";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
	}

	@Override
	public Optional<User> getUser(Integer id) {
		String sql = "select id, name from user where id=?";
		try {
			User user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), id);
			return Optional.of(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Optional.of(null);
	}
	
}
