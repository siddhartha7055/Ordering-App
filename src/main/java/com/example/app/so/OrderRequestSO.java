package com.example.app.so;

import java.util.List;

import com.example.app.entity.Customer;
import com.example.app.entity.Order;
import com.example.app.entity.OrderItem;

public class OrderRequestSO {
	private Customer customer;
	private Order order;
	private List<OrderItem> orderItems;

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

}
