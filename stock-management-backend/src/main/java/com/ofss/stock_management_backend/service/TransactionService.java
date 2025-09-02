package com.ofss.stock_management_backend.service;

import org.springframework.stereotype.Service;

import com.ofss.stock_management_backend.model.Customer;
import com.ofss.stock_management_backend.model.Stock;
import com.ofss.stock_management_backend.model.Transaction;
import com.ofss.stock_management_backend.repository.TransactionRepository;

import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    // Fetch transactions by Customer entity
    public List<Transaction> getTransactionsByCustomer(Customer customer) {
        return transactionRepository.findByCustomer(customer);
    }

    // Fetch transactions by Stock entity
    public List<Transaction> getTransactionsByStock(Stock stock) {
        return transactionRepository.findByStock(stock);
    }

    // Fetch transactions by both Customer and Stock entities
    public List<Transaction> getTransactionsByCustomerAndStock(Customer customer, Stock stock) {
        return transactionRepository.findByCustomerAndStock(customer, stock);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}