package com.example.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.services.OrderService;
import com.example.app.so.OrderReponseListSO;
import com.example.app.so.OrderRequestListSO;

@RestController
@RequestMapping(value = "/api")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@RequestMapping(value = "/orders", method = RequestMethod.POST)
	public ResponseEntity<OrderReponseListSO> placeOrderWithItems(@RequestBody OrderRequestListSO request) {
		try {
			OrderReponseListSO order = orderService.placeOrderWithItems(request);
			return ResponseEntity.ok(order);
		} catch (Exception e) {
			// Handle exception and return appropriate response
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@RequestMapping(value = "/orderHistory", method = RequestMethod.POST)
	public ResponseEntity<Object> getOrderHistoryByCustomerId(@RequestParam String email) {
		System.out.println("email");
		try {
			System.out.println("email");
			OrderReponseListSO orderHistory = orderService.getOrderHistory(email);
			System.out.println("email");
			return ResponseEntity.ok(orderHistory);
		} catch (Exception e) {
			// Handle exception and return appropriate response
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
