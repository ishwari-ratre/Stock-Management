package com.ofss.stock_management_backend.service;

import com.ofss.stock_management_backend.dto.DashboardResponse;
import com.ofss.stock_management_backend.model.Customer;
import com.ofss.stock_management_backend.model.Transaction;
import com.ofss.stock_management_backend.model.TransactionType;
import com.ofss.stock_management_backend.repository.CustomerRepository;
import com.ofss.stock_management_backend.repository.TransactionRepository;
import com.ofss.stock_management_backend.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
        List<Transaction> lastTransactions = transactionRepository
                .findTop3ByCustomer_EmailIdOrderByTimestampDesc(email);

        // Portfolio calculations
        double invested = 0;
        double currentValue = 0;

        List<Transaction> allTransactions = transactionRepository.findByCustomerEmailId(email);

        for (Transaction t : allTransactions) {
            if (t.getType() == TransactionType.BUY) {
                invested += t.getQuantity() * t.getPriceAtTransaction();
                currentValue += t.getQuantity() * t.getStock().getBasePrice(); // live price
            }
        }

        double profitLoss = currentValue - invested;
        double percentProfitLoss = invested > 0 ? (profitLoss / invested) * 100 : 0;

        return new DashboardResponse(customer, lastTransactions, invested, profitLoss, percentProfitLoss);
    }
}
