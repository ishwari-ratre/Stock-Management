package com.ofss.repository;

import com.ofss.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer> {
    // Example of custom finder
    Customer findByEmail(String email);
}
