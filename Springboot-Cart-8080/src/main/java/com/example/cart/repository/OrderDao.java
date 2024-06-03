package com.example.cart.repository;

import java.util.List;

import com.example.cart.model.po.Item;
import com.example.cart.model.po.Order;

public interface OrderDao {
	public List<Order> getAllOrders();
	public List<Order> getOrdersByCustomerId(Integer customerId);
	public Order getOrderById(Integer orderId);
	public Item getItemById(Integer itemId);
	public List<Item> getItemsByOrderId(Integer orderId);
	// order 中是否有指定 productId 的商品
	public Boolean isProductInOrder(Integer productId);
	// 找到該客戶尚未 checkout 的訂單
	public Order getOrderByCustomerIdAndUncheckout(Integer customerId);
	
	public Order addOrder(Order order);
	public Item addOrReduceOrderItem(Integer orderId, Integer productId, Integer amount);
	public Order updateOrder(Order order);
	public Boolean deleteOrder(Integer orderId);
	public Boolean deleteOrderItem(Integer itemId);
	
	public Boolean checkoutOrderById(Integer orderId);
}
