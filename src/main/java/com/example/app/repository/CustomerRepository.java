package com.example.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.app.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	@Query("SELECT c FROM Customer c WHERE c.email = :email")
	Optional<Customer> findByEmail(@Param("email") String email);
	
	@Query("SELECT c FROM Customer c WHERE c.contactInfo = :num")
	Optional<Customer> findByPhone(@Param("num") String num);

}
