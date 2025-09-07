package com.ofss.stock_management_backend.service;

import com.ofss.stock_management_backend.dto.PortfolioResponse;
import com.ofss.stock_management_backend.dto.TradeRequest;
import com.ofss.stock_management_backend.model.Customer;
import com.ofss.stock_management_backend.model.Stock;
import com.ofss.stock_management_backend.model.Transaction;
import com.ofss.stock_management_backend.model.TransactionType;
import com.ofss.stock_management_backend.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
        transaction.setPriceAtTransaction(stock.getBasePrice());
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
        transaction.setPriceAtTransaction(stock.getBasePrice());
        transaction.setType(TransactionType.SELL);
        transaction.setTimestamp(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    // All transactions for a customer
    public List<Transaction> getTransactionHistory(String emailId) {
        return transactionRepository.findByCustomerEmailId(emailId);
    }

    // Current portfolio (symbol -> quantity)
public List<PortfolioResponse> getPortfolioDetails(String emailId) {
    List<Transaction> transactions = transactionRepository.findByCustomerEmailId(emailId);

    // Map stock → (quantity, invested amount)
    Map<Stock, PortfolioTracker> trackerMap = new HashMap<>();

    for (Transaction tx : transactions) {
        Stock stock = tx.getStock();
        int qty = tx.getQuantity();
        double price = tx.getPriceAtTransaction();

        PortfolioTracker tracker = trackerMap.getOrDefault(stock, new PortfolioTracker());

        if (tx.getType() == TransactionType.BUY) {
            tracker.quantity += qty;
            tracker.invested += qty * price;
        } else if (tx.getType() == TransactionType.SELL) {
            tracker.quantity -= qty;
            tracker.invested -= qty * price; // subtract invested value
            if (tracker.quantity < 0) tracker.quantity = 0; // safety
            if (tracker.invested < 0) tracker.invested = 0.0; // safety
        }

        trackerMap.put(stock, tracker);
    }

    List<PortfolioResponse> portfolio = new ArrayList<>();

    for (Map.Entry<Stock, PortfolioTracker> entry : trackerMap.entrySet()) {
        Stock stock = entry.getKey();
        PortfolioTracker tracker = entry.getValue();

        // Skip invalid/empty holdings
        if (tracker == null || tracker.quantity <= 0) continue;

        double avgPriceAtTransaction = tracker.quantity > 0
                ? tracker.invested / tracker.quantity
                : 0.0;

        double currentPrice = stock.getBasePrice();
        double totalValue = tracker.quantity * currentPrice;
        double profitLoss = totalValue - (tracker.quantity * avgPriceAtTransaction);

        portfolio.add(new PortfolioResponse(
                stock,
                tracker.quantity,
                currentPrice,
                avgPriceAtTransaction,
                totalValue,
                profitLoss
        ));
    }

    return portfolio;
}

static class PortfolioTracker {
    int quantity = 0;
    double invested = 0.0;
}


}
