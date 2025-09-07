package com.ofss.stock_management_backend.dto;

import com.ofss.stock_management_backend.model.Transaction;
import com.ofss.stock_management_backend.model.Customer;
import java.util.List;

public class DashboardResponse {
    private Customer customer;
    private List<Transaction> lastTransactions;
    private double invested;
    private double profitLoss;
    private double percentProfitLoss;

    public DashboardResponse(Customer customer, List<Transaction> lastTransactions,
                             double invested, double profitLoss, double percentProfitLoss) {
        this.customer = customer;
        this.lastTransactions = lastTransactions;
        this.invested = invested;
        this.profitLoss = profitLoss;
        this.percentProfitLoss = percentProfitLoss;
    }

    // Getters & Setters
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public List<Transaction> getLastTransactions() { return lastTransactions; }
    public void setLastTransactions(List<Transaction> lastTransactions) { this.lastTransactions = lastTransactions; }

    public double getInvested() { return invested; }
    public void setInvested(double invested) { this.invested = invested; }

    public double getProfitLoss() { return profitLoss; }
    public void setProfitLoss(double profitLoss) { this.profitLoss = profitLoss; }

    public double getPercentProfitLoss() { return percentProfitLoss; }
    public void setPercentProfitLoss(double percentProfitLoss) { this.percentProfitLoss = percentProfitLoss; }
}
