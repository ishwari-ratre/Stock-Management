package com.ofss.service;

import org.springframework.stereotype.Service;
import com.ofss.model.Transaction;
import com.ofss.repository.TransactionRepository;
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

    public Transaction getTransactionById(int id) {
        return transactionRepository.findById(id).orElse(null);
    }

    public List<Transaction> getTransactionsByCustomer(int customerId) {
        return transactionRepository.findByCustomerId(customerId);
    }

    public List<Transaction> getTransactionsByStock(int stockId) {
        return transactionRepository.findByStockId(stockId);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public void deleteTransaction(int id) {
        transactionRepository.deleteById(id);
    }
}
