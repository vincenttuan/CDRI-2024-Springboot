package com.example.cart.repository;

import java.sql.PreparedStatement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.example.cart.model.po.Product;

@Repository("InMySQLProduct")
public class ProductDaoInMySQL implements ProductDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private RowMapper<Product> rowMapper = new BeanPropertyRowMapper<>(Product.class);
	
	@Override
	public List<Product> queryAllProducts() {
		String sql = "select id, name, cost, price, qty from product order by id";
		return jdbcTemplate.query(sql, rowMapper);
	}

	@Override
	public Product getProductById(Integer id) {
		String sql = "select id, name, cost, price, qty from product where id = ?";
		try {
			Product product = jdbcTemplate.queryForObject(sql, rowMapper, id);
			return product;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Product addProduct(Product product) {
		String sql = "insert into product (name, cost, price, qty) values (?, ?, ?, ?)";
		// 獲取自增 id 主鍵值
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		
		jdbcTemplate.update(conn -> {
			PreparedStatement ps = conn.prepareStatement(sql, new String[] {"id"}); // 主鍵名: id
			ps.setString(1, product.getName());
			ps.setInt(2, product.getCost());
			ps.setInt(3, product.getPrice());
			ps.setInt(4, product.getQty());
			return ps;
		}, keyHolder);
		
		// id 會放在 keyHolder 中
		product.setId(keyHolder.getKey().intValue());
		return product;
	}

	@Override
	public Product updateProduct(Product product) {
		String sql = "update product set name=?, cost=?, price=?, qty=? where id=?";
		int rowcount = jdbcTemplate.update(sql, product.getName(), product.getCost(), product.getPrice(), product.getQty(), product.getId());
		return rowcount > 0 ? product : null;
	}

	@Override
	public Product updateProductQty(Integer id, Integer qty) {
		String sql = "update product set qty = ? where id = ?";
		int rowcount = jdbcTemplate.update(sql, qty, id);
		return rowcount > 0 ? getProductById(id) : null;
	}

	@Override
	public Product addProductQty(Integer id, Integer increment) {
		String sql = "update product set qty = qty + ? where id = ?";
		int rowcount = jdbcTemplate.update(sql, increment, id);
		return rowcount > 0 ? getProductById(id) : null;
	}

	@Override
	public Product reduceProductQty(Integer id, Integer decrement) {
		String sql = "update product set qty = qty - ? where id = ?";
		int rowcount = jdbcTemplate.update(sql, decrement, id);
		return rowcount > 0 ? getProductById(id) : null;
	}

	@Override
	public Boolean deleteProductById(Integer id) {
		String sql = "delete from product where id = ?";
		
		try {
			int rowcount = jdbcTemplate.update(sql, id);
			return rowcount > 0;
		} catch (Exception e) { // 若關聯存在則無法刪除
			e.printStackTrace();
			return false;
		}
		
	}

}
