package com.ofss.stock_management_backend.service;

import com.ofss.stock_management_backend.dto.DashboardResponse;
import com.ofss.stock_management_backend.model.Customer;
import com.ofss.stock_management_backend.model.Stock;
import com.ofss.stock_management_backend.model.Transaction;
import com.ofss.stock_management_backend.model.TransactionType;
import com.ofss.stock_management_backend.repository.CustomerRepository;
import com.ofss.stock_management_backend.repository.TransactionRepository;
import com.ofss.stock_management_backend.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    private final StockRepository stockRepository;

    public DashboardService(CustomerRepository customerRepository,
                            TransactionRepository transactionRepository,
                            StockRepository stockRepository) {
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
        this.stockRepository = stockRepository;
    }

    public DashboardResponse getDashboard(String email) {
    Customer customer = customerRepository.findByEmailId(email);
    if (customer == null) {
        throw new RuntimeException("Customer not found with email: " + email);
    }

    // Last 3 transactions
    List<Transaction> lastTransactions =
            transactionRepository.findTop3ByCustomer_EmailIdOrderByTimestampDesc(email);

    // Track portfolio per stock
    Map<Stock, PortfolioTracker> trackerMap = new HashMap<>();
    List<Transaction> allTransactions = transactionRepository.findByCustomerEmailId(email);

    for (Transaction t : allTransactions) {
        Stock stock = t.getStock();
        PortfolioTracker tracker = trackerMap.getOrDefault(stock, new PortfolioTracker());

        if (t.getType() == TransactionType.BUY) {
            tracker.quantity += t.getQuantity();
            tracker.invested += t.getQuantity() * t.getPriceAtTransaction();
        } else if (t.getType() == TransactionType.SELL) {
            tracker.quantity -= t.getQuantity();
            tracker.invested -= t.getQuantity() * t.getPriceAtTransaction();
        }

        trackerMap.put(stock, tracker);
    }

    double invested = 0;
    double currentValue = 0;

    for (Map.Entry<Stock, PortfolioTracker> entry : trackerMap.entrySet()) {
        PortfolioTracker tracker = entry.getValue();

        if (tracker.quantity <= 0) continue; // skip sold-out stocks

        double avgPrice = tracker.invested / tracker.quantity;
        double currentPrice = entry.getKey().getBasePrice();

        invested += tracker.quantity * avgPrice;
        currentValue += tracker.quantity * currentPrice;
    }

    double profitLoss = currentValue - invested;
    double percentProfitLoss = invested > 0 ? (profitLoss / invested) * 100 : 0;

    return new DashboardResponse(customer, lastTransactions, invested, profitLoss, percentProfitLoss);
}

static class PortfolioTracker {
    int quantity = 0;
    double invested = 0.0;
}

}
