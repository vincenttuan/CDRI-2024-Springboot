package com.example.cart.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import com.example.cart.model.po.Item;
import com.example.cart.model.po.Order;

import java.util.List;

@Repository("InMySQLOrder")
public class OrderDaoInMySQL implements OrderDao {

    @Autowired
    private JdbcTemplate jdbcTemplate; // 自動注入 JdbcTemplate 以用於資料庫操作

    // 定義將 ResultSet 中的數據映射到 Order 對象的映射規則
    private RowMapper<Order> orderRowMapper = (rs, rowNum) -> new Order(
            rs.getInt("id"),
            rs.getString("date"),
            rs.getInt("checkout"),
            rs.getInt("customerId")
    );

    // 定義將 ResultSet 中的數據映射到 Item 對象的映射規則
    private RowMapper<Item> itemRowMapper = (rs, rowNum) -> new Item(
            rs.getInt("id"),
            rs.getInt("orderId"),
            rs.getInt("productId"),
            rs.getInt("amount")
    );

    // 獲取所有訂單
    @Override
    public List<Order> getAllOrders() {
        String sql = "SELECT id, date, checkout, customerId FROM `Order`";
        return jdbcTemplate.query(sql, orderRowMapper);
    }

    // 根據客戶ID獲取訂單
    @Override
    public List<Order> getOrdersByCustomerId(Integer customerId) {
        String sql = "SELECT id, date, checkout, customerId FROM `Order` WHERE customerId = ?";
        return jdbcTemplate.query(sql, orderRowMapper, customerId);
    }

    // 根據訂單ID獲取單個訂單
    @Override
    public Order getOrderById(Integer orderId) {
        String sql = "SELECT id, date, checkout, customerId FROM `Order` WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, orderRowMapper, orderId);
        } catch (Exception e) {
            return null; // 查詢失敗時返回 null
        }
    }

    // 根據項目ID獲取單個項目
    @Override
    public Item getItemById(Integer itemId) {
        String sql = "SELECT id, orderId, productId, amount FROM `Item` WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, itemRowMapper, itemId);
    }

    // 根據訂單ID獲取該訂單的所有項目
    @Override
    public List<Item> getItemsByOrderId(Integer orderId) {
        String sql = "SELECT id, orderId, productId, amount FROM `Item` WHERE orderId = ?";
        return jdbcTemplate.query(sql, itemRowMapper, orderId);
    }

    // 新增訂單
    @Override
    public Order addOrder(Order order) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO `Order` (date, customerId) VALUES (?, ?)";
        jdbcTemplate.update(con -> {
            var ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, order.getDate());
            ps.setInt(2, order.getCustomerId());
            return ps;
        }, keyHolder);
        order.setId(keyHolder.getKey().intValue()); // 設置訂單ID
        return order;
    }

    // 增加或減少訂單項目的數量，若項目不存在則新增
    @Override
    public Item addOrReduceOrderItem(Integer orderId, Integer productId, Integer amount) {
        String checkSql = "SELECT COUNT(*) FROM `Item` WHERE orderId = ? AND productId = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, orderId, productId);

        if (count != null && count > 0) {
            String updateSql = "UPDATE `Item` SET amount = amount + ? WHERE orderId = ? AND productId = ?";
            jdbcTemplate.update(updateSql, amount, orderId, productId);
        } else {
            String insertSql = "INSERT INTO `Item` (orderId, productId, amount) VALUES (?, ?, ?)";
            jdbcTemplate.update(insertSql, orderId, productId, amount);
        }

        String deleteSql = "DELETE FROM `Item` WHERE orderId = ? AND productId = ? AND amount = 0";
        int rowcount = jdbcTemplate.update(deleteSql, orderId, productId); // 若數量為 0，則刪除該項目
        if(rowcount > 0) {
        	return null;
        }
        String selectSql = "SELECT * FROM `Item` WHERE orderId = ? AND productId = ?";
        return jdbcTemplate.queryForObject(selectSql, itemRowMapper, orderId, productId); // 返回更新後的項目
    }

    // 更新訂單資訊
    @Override
    public Order updateOrder(Order order) {
        String sql = "UPDATE `Order` SET date = ?, customerId = ? WHERE id = ?";
        jdbcTemplate.update(sql, order.getDate(), order.getCustomerId(), order.getId());
        return order;
    }

    // 刪除指定ID的訂單
    @Override
    public Boolean deleteOrder(Integer orderId) {
        String sql = "DELETE FROM `Order` WHERE id = ?";
        jdbcTemplate.update(sql, orderId);
        return true;
    }

    // 刪除指定ID的訂單項目
    @Override
    public Boolean deleteOrderItem(Integer itemId) {
        String sql = "DELETE FROM `Item` WHERE id = ?";
        jdbcTemplate.update(sql, itemId);
        return true;
    }

    // 檢查產品是否在任何訂單項目中
    @Override
    public Boolean isProductInOrder(Integer productId) {
        String sql = "SELECT COUNT(*) FROM `Item` WHERE productId = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, productId);
        return count != null && count > 0;
    }

    // 根據客戶ID查詢尚未 checkout 的訂單
    @Override
    public Order getOrderByCustomerIdAndUncheckout(Integer customerId) {
        String sql = "SELECT id, date, checkout, customerId FROM `Order` WHERE customerId = ? AND checkout = ?";
        try {
            return jdbcTemplate.queryForObject(sql, orderRowMapper, customerId, 0);
        } catch (Exception e) {
            return null; // 查詢失敗時返回 null
        }
    }

	@Override
	public Boolean checkoutOrderById(Integer orderId) {
		String sql = "UPDATE `Order` SET checkout = ? WHERE id = ?";
        return jdbcTemplate.update(sql, 1, orderId) > 0;
	}
}
