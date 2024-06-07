package com.example.demo.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Repository;

import com.example.demo.model.po.Item;
import com.example.demo.model.po.Order;

@Repository
public class OrderDao {
	
	private static List<Order> orders = new CopyOnWriteArrayList<>();
	
	static {
		// 訂單編號, 客戶編號, 訂單日期, 訂單明細(項目), 總計
		Order order1 = new Order(1, 2, "2024-01-01", new ArrayList<>(List.of(
				// 訂單明細(項目)編號, 訂單主檔編號, 產品編號, 訂購數量, 小計
				new Item(1, 1, 1, 5, 0),
				new Item(2, 1, 3, 2, 0)
		)), 0);
		
		// 訂單編號, 客戶編號, 訂單日期, 訂單明細(項目), 總計
		Order order2 = new Order(2, 1, "2024-01-02", new ArrayList<>(List.of(
				// 訂單明細(項目)編號, 訂單主檔編號, 產品編號, 訂購數量, 小計
				new Item(1, 2, 2, 4, 0),
				new Item(2, 2, 3, 1, 0)
		)), 0);
		
		// 訂單編號, 客戶編號, 訂單日期, 訂單明細(項目), 總計
		Order order3 = new Order(3, 3, "2024-01-03", new ArrayList<>(List.of(
				// 訂單明細(項目)編號, 訂單主檔編號, 產品編號, 訂購數量, 小計
				new Item(1, 3, 1, 2, 0),
				new Item(2, 3, 2, 4, 0),
				new Item(3, 3, 3, 1, 0)
		)), 0);
		
		// 訂單編號, 客戶編號, 訂單日期, 訂單明細(項目), 總計
		Order order4 = new Order(4, 3, "2024-01-04", new ArrayList<>(List.of(
				// 訂單明細(項目)編號, 訂單主檔編號, 產品編號, 訂購數量, 小計
				new Item(1, 4, 1, 7, 0)
		)), 0);
		
		orders.add(order1);
		orders.add(order2);
		orders.add(order3);
		orders.add(order4);
	}
	
	// 查詢所有訂單
	public List<Order> getAllOrders() {
		return orders;
	}
	
	// 查詢單筆訂單
	public Order getOrderById(Integer id) {
		return orders.stream()
				.filter(order -> order.getId().equals(id))
				.findFirst()
				.orElseGet(null);
	}
	
	// 查詢單筆訂單項目
	public Item getOrderItemById(Integer orderId, Integer itemId) {
		Order order = getOrderById(orderId);
		if(order == null || order.getItems().size() == 0) {
			return null;
		}
		try {
			return order.getItems().stream()
					.filter(item -> item.getId().equals(itemId))
					.findFirst()
					.orElseGet(null);
		} catch (Exception e) {
			return null;
		}
	}
	
	// 新增訂單
	public Order addOrder(Order order) {
		// 獲取 orders 中的最大 id
		int maxId = orders.stream().mapToInt(Order::getId).max().orElse(0);
		int id = maxId + 1;
		order.setId(id);
		orders.add(order);
		return order;
	}
	
	// 新增訂單項目
	public Item addOrderItem(Integer orderId, Item item) {
		Order order = getOrderById(orderId);
		if(order == null) {
			return null;
		}
		// 獲取 items 中的最大 id
		int maxItemId = order.getItems().stream().mapToInt(Item::getId).max().orElse(0);
		int itemId = maxItemId + 1;
		item.setId(itemId);
		item.setOrderId(orderId);
		order.getItems().add(item); // 將訂單細目加入到訂單主檔
		return item;
	}
	
	// 修改訂單項目的數量
	public Item updateOrderItemQuantity(Integer orderId, Integer itemId, Integer quantity) {
		Item item = getOrderItemById(orderId, itemId);
		if(item == null) {
			return null;
		}
		item.setQuantity(quantity);
		return item;
	}
	
	// 刪除訂單
	public Boolean deleteOrder(Integer id) {
		return orders.removeIf(order -> order.getId().equals(id));
	}
	
	// 刪除訂單項目
	public Boolean deleteOrderItem(Integer orderId, Integer itemId) {
		Order order = getOrderById(orderId);
		if(order == null) {
			return false;
		}
		return order.getItems().removeIf(item -> item.getId().equals(itemId));
	}
	
}
