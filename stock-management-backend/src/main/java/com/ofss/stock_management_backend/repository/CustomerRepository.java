package com.ofss.stock_management_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ofss.stock_management_backend.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    // Example of custom finder
    Customer findByEmailId(String email);

    void deleteByEmailId(String email);
}
