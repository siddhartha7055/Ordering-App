package com.example.app.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import com.example.app.entity.Customer;
import com.example.app.entity.Order;
import com.example.app.entity.OrderItem;
import com.example.app.repository.CustomerRepository;
import com.example.app.repository.OrderItemRepository;
import com.example.app.repository.OrderRepository;
import com.example.app.so.OrderReponseListSO;
import com.example.app.so.OrderRequestListSO;
import com.example.app.so.OrderRequestSO;
import com.example.app.so.OrderResponseSO;
import com.example.app.validator.CustomerOrderValidator;

import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;

@Service
public class OrderService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Transactional
	public OrderReponseListSO placeOrderWithItems(OrderRequestListSO curlRequest) {
		OrderReponseListSO orderSO = new OrderReponseListSO();
		List<OrderResponseSO> orderDetailsList = new ArrayList<>();
		CustomerOrderValidator orderValidator = new CustomerOrderValidator();
		Errors errors = new BeanPropertyBindingResult(curlRequest, "orderRequestList");
		orderValidator.validate(curlRequest, errors);
		if (errors.hasErrors()) {
			StringBuilder errorMessageBuilder = new StringBuilder();
			List<FieldError> fieldErrors = errors.getFieldErrors();
			for (FieldError fieldError : fieldErrors) {
				String field = fieldError.getField();
				String message = fieldError.getDefaultMessage();
				errorMessageBuilder.append("Field: ").append(field).append(", Error: ").append(message).append("; ");
			}
			orderSO.setMessage(errorMessageBuilder.toString());
		} else {
			try {
				for (OrderRequestSO request : curlRequest.getOrderRequestSO()) {
					Customer customer = request.getCustomer();
					Order order = request.getOrder();
					List<OrderItem> orderItems = request.getOrderItems();

					System.out.println(customer.getEmail() + " customer.getEmail()");

					// Check if the customer already exists in the database
					Optional<Customer> existingCustomer = customerRepository.findByEmail(customer.getEmail());

					if (existingCustomer.isPresent()) {
						System.out.println("present");
						// Customer already exists, use the existing customer
						customer = existingCustomer.get();
					} else {
						// Save the new customer
						System.out.println("new");
						customer = customerRepository.save(customer);
					}

					System.out.println("Cust " + customer);

					// Set customer in the order
					order.setCustomer(customer);

					// Get the total count of orders for the customer
					int orderCount = orderRepository.countByCustomerId(customer.getId());
					System.out.println("Order Count " + orderCount);

					// Calculate the discount based on the order count
					BigDecimal discount = BigDecimal.ZERO;
					if (orderCount >= 10 && orderCount < 20) {
						discount = order.getTotalAmount().multiply(BigDecimal.valueOf(0.10)); // 10% discount
					} else if (orderCount >= 20) {
						discount = order.getTotalAmount().multiply(BigDecimal.valueOf(0.20)); // 20% discount
					}

					// Apply the BFCM discount of 15% if applicable
					int currentMonth = LocalDate.now().getMonthValue();
					System.out.println("currentMonth " + currentMonth);
					if (currentMonth == 11) {
						BigDecimal bfcmDiscount = order.getTotalAmount().multiply(BigDecimal.valueOf(0.15)); // 15% BFCM
																												// discount

						discount = discount.add(bfcmDiscount);
					}

					// Apply the discount to the order total amount
					BigDecimal discountedAmount = order.getTotalAmount().subtract(discount);
					order.setDiscount(discount);
					order.setTotalAmount(discountedAmount);

					// Set order in each order item
					for (OrderItem orderItem : orderItems) {
						orderItem.setOrder(order);
					}

					// Save the order and order items
					order.setOrderItems(orderItems);
					order = orderRepository.save(order);

					// Create order details DTO and add it to the list
					OrderResponseSO orderDetails = new OrderResponseSO();
					orderDetails.setOrderId(order.getId()); // Set the order ID after it is generated
					orderDetails.setCustomerName(customer.getName());
					orderDetails.setTotalAmount(order.getTotalAmount());
					orderDetails.setDiscount(order.getDiscount());
					orderDetailsList.add(orderDetails);
				}
				orderSO.setMessage("Order Placed Successfully");
				orderSO.setOrderDetailsList(orderDetailsList);
			} catch (Exception e) {
				e.printStackTrace();
				orderSO.setMessage("Some Error Occured while placing your order please try again later");
			}
		}
		return orderSO;
	}

	public OrderReponseListSO getOrderHistory(String email) {
		OrderReponseListSO orderResponseListSO = new OrderReponseListSO();
		if (StringUtils.isNotBlank(email) && isValidEmail(email)) {
			List<OrderResponseSO> orderDetailsList = new ArrayList<>();
			Optional<Customer> existingCustomer = customerRepository.findByEmail(email);

			if (existingCustomer.isPresent()) {
				List<Order> orderHistory = orderRepository.findByCustomerId(existingCustomer.get().getId());

				if (!orderHistory.isEmpty()) {
					for (Order order : orderHistory) {
						OrderResponseSO orderDetails = new OrderResponseSO();
						orderDetails.setOrderId(order.getId());
						orderDetails.setCustomerName(existingCustomer.get().getName());
						orderDetails.setTotalAmount(order.getTotalAmount());
						orderDetails.setDiscount(order.getDiscount());
						orderDetailsList.add(orderDetails);
					}
					orderResponseListSO.setMessage("Orders Found");
					orderResponseListSO.setOrderDetailsList(orderDetailsList);
				} else {
					orderResponseListSO.setMessage("No Data Found for " + existingCustomer.get().getName());
				}
			} else {
				orderResponseListSO.setMessage("No User Found");
			}
		} else {
			orderResponseListSO.setMessage("Email format is wrong :" + email);
		}
		return orderResponseListSO;
	}

	public Long checkCust(String email) {
		// TODO Auto-generated method stub
		Optional<Customer> existingCustomer = customerRepository.findByEmail(email);
		if (existingCustomer.isPresent()) {
			return existingCustomer.get().getId();
		}
		return null;
	}

	public boolean isStringOnly(String variable) {
		return variable != null && variable.matches("^[a-zA-Z]+$");
	}

	public boolean isValidEmail(String email) {
		String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
		Pattern pattern = Pattern.compile(emailRegex);
		return pattern.matcher(email).matches();
	}

}
