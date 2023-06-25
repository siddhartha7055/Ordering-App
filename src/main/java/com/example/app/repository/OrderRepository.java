package com.example.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.app.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	int countByCustomerId(Long customerId);

	List<Order> findByCustomerId(Long id);
}
