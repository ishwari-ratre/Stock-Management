package com.ofss.stock_management_backend.service;

import com.ofss.stock_management_backend.dto.TradeRequest;
import com.ofss.stock_management_backend.model.Customer;
import com.ofss.stock_management_backend.model.Stock;
import com.ofss.stock_management_backend.model.Transaction;
import com.ofss.stock_management_backend.model.TransactionType;
import com.ofss.stock_management_backend.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CustomerService customerService;
    private final StockService stockService;

    public TransactionService(TransactionRepository transactionRepository,
            CustomerService customerService,
            StockService stockService) {
        this.transactionRepository = transactionRepository;
        this.customerService = customerService;
        this.stockService = stockService;
    }

    public Transaction buyStock(TradeRequest tradeRequest) {
        Customer customer = customerService.getCustomerByEmail(tradeRequest.getEmail());
        if (customer == null) {
            throw new RuntimeException("Customer not found!");
        }

        Stock stock = stockService.getStockBySymbol(tradeRequest.getSymbol());
        if (stock == null) {
            throw new RuntimeException("Stock not found!");
        }

        Transaction transaction = new Transaction();
        transaction.setCustomer(customer);
        transaction.setStock(stock);
        transaction.setQuantity(tradeRequest.getQuantity());
        transaction.setPriceAtTransaction(transaction.getPriceAtTransaction());
        transaction.setType(TransactionType.BUY);
        transaction.setTimestamp(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    public Transaction sellStock(TradeRequest tradeRequest) {
        Customer customer = customerService.getCustomerByEmail(tradeRequest.getEmail());
        if (customer == null) {
            throw new RuntimeException("Customer not found!");
        }

        Stock stock = stockService.getStockBySymbol(tradeRequest.getSymbol());
        if (stock == null) {
            throw new RuntimeException("Stock not found!");
        }

        // (optional: check if customer has enough stock before selling)

        Transaction transaction = new Transaction();
        transaction.setCustomer(customer);
        transaction.setStock(stock);
        transaction.setQuantity(tradeRequest.getQuantity());
        transaction.setPriceAtTransaction(transaction.getPriceAtTransaction());
        transaction.setType(TransactionType.SELL);
        transaction.setTimestamp(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }
}
