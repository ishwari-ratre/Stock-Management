package com.ofss.repository;

import com.ofss.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Integer> {
    // Get all transactions for a customer
    List<Transaction> findByCustomerId(Long customerId);

    // Get all transactions for a stock
    List<Transaction> findByStockId(Long stockId);

    // Get transactions for customer + stock
    List<Transaction> findByCustomerIdAndStockId(Long customerId, Long stockId);
}
