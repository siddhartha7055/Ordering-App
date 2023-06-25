package com.example.app.so;

import java.util.List;

public class OrderReponseListSO {

	private String message;
	private List<OrderResponseSO> orderDetailsList;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<OrderResponseSO> getOrderDetailsList() {
		return orderDetailsList;
	}

	public void setOrderDetailsList(List<OrderResponseSO> orderDetailsList) {
		this.orderDetailsList = orderDetailsList;
	}

}
