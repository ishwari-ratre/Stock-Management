package com.ofss.stock_management_backend.service;

import com.ofss.stock_management_backend.dto.TradeRequest;
import com.ofss.stock_management_backend.model.Customer;
import com.ofss.stock_management_backend.model.Stock;
import com.ofss.stock_management_backend.model.Transaction;
import com.ofss.stock_management_backend.model.TransactionType;
import com.ofss.stock_management_backend.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            System.out.println("Stock not found--------- " + tradeRequest.getSymbol());
            throw new RuntimeException("Stock not found!");
        }

        Transaction transaction = new Transaction();
        transaction.setCustomer(customer);
        transaction.setStock(stock);
        transaction.setQuantity(tradeRequest.getQuantity());
        // price needs to be taken from stock latest price instead of transaction
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

        // Calculate how many shares the customer currently owns
        int bought = transactionRepository.findByCustomerAndStockAndType(customer, stock, TransactionType.BUY)
                .stream()
                .mapToInt(Transaction::getQuantity)
                .sum();

        int sold = transactionRepository.findByCustomerAndStockAndType(customer, stock, TransactionType.SELL)
                .stream()
                .mapToInt(Transaction::getQuantity)
                .sum();

        int owned = bought - sold;

        if (tradeRequest.getQuantity() > owned) {
            throw new RuntimeException("Insufficient stock to sell! Owned: " + owned
                    + ", Tried to sell: " + tradeRequest.getQuantity());
        }

        Transaction transaction = new Transaction();
        transaction.setCustomer(customer);
        transaction.setStock(stock);
        transaction.setQuantity(tradeRequest.getQuantity());
        transaction.setPriceAtTransaction(transaction.getPriceAtTransaction());
        transaction.setType(TransactionType.SELL);
        transaction.setTimestamp(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    // All transactions for a customer
    public List<Transaction> getTransactionHistory(String emailId) {
        return transactionRepository.findByCustomerEmailId(emailId);
    }

    // Current portfolio (symbol -> quantity)
    public Map<String, Integer> getPortfolio(String emailId) {
        List<Transaction> transactions = transactionRepository.findByCustomerEmailId(emailId);
        Map<String, Integer> holdings = new HashMap<>();

        for (Transaction tx : transactions) {
            String symbol = tx.getStock().getSymbol();
            int qty = tx.getQuantity();

            if (tx.getType() == TransactionType.BUY) {
                holdings.put(symbol, holdings.getOrDefault(symbol, 0) + qty);
            } else if (tx.getType() == TransactionType.SELL) {
                holdings.put(symbol, holdings.getOrDefault(symbol, 0) - qty);
            }
        }

        // clean zero/negative
        holdings.entrySet().removeIf(e -> e.getValue() <= 0);

        return holdings;
    }
}
