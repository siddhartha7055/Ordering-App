package com.example.app.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.example.app.so.OrderRequestListSO;
import com.example.app.so.OrderRequestSO;

public class CustomerOrderValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
		return OrderRequestListSO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "orderRequestSO", "field.required",
                "Order request list must not be empty");

        OrderRequestListSO orderRequestList = (OrderRequestListSO) target;
        if (orderRequestList.getOrderRequestSO() != null) {
            for (int i = 0; i < orderRequestList.getOrderRequestSO().size(); i++) {
                String prefix = "orderRequestSO[" + i + "]";
                OrderRequestSO orderRequest = orderRequestList.getOrderRequestSO().get(i);

                ValidationUtils.rejectIfEmptyOrWhitespace(errors, prefix + ".customer.name", "field.required",
                        "Customer name is required");
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, prefix + ".customer.email", "field.required",
                        "Customer email is required");

                // Perform additional validations if needed
                // ...

                if (orderRequest.getOrder() == null) {
                    errors.rejectValue(prefix + ".order", "field.required", "Order is required");
                }

                if (orderRequest.getOrderItems() == null || orderRequest.getOrderItems().isEmpty()) {
                    errors.rejectValue(prefix + ".orderItems", "field.required", "Order items are required");
                }
            }
        }
    }
}

