package com.ofss.stock_management_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ofss.stock_management_backend.model.Customer;
import com.ofss.stock_management_backend.model.Stock;
import com.ofss.stock_management_backend.model.Transaction;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCustomer(Customer customer);

    List<Transaction> findByStock(Stock stock);

    List<Transaction> findByCustomerAndStock(Customer customer, Stock stock);
}
