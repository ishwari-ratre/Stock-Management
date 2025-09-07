package com.ofss.stock_management_backend.dto;

import com.ofss.stock_management_backend.model.Stock;

public class PortfolioResponse {

    private Stock stock;
    private int quantity;
    private double currentPrice;
    private double avgPriceAtTransaction;
    private double totalValue;
    private double profitLoss;

    public PortfolioResponse() {
    }

    public PortfolioResponse(Stock stock, int quantity, double currentPrice,
                             double avgPriceAtTransaction, double totalValue, double profitLoss) {
        this.stock = stock;
        this.quantity = quantity;
        this.currentPrice = currentPrice;
        this.avgPriceAtTransaction = avgPriceAtTransaction;
        this.totalValue = totalValue;
        this.profitLoss = profitLoss;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getAvgPriceAtTransaction() {
        return avgPriceAtTransaction;
    }

    public void setAvgPriceAtTransaction(double avgPriceAtTransaction) {
        this.avgPriceAtTransaction = avgPriceAtTransaction;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

    public double getProfitLoss() {
        return profitLoss;
    }

    public void setProfitLoss(double profitLoss) {
        this.profitLoss = profitLoss;
    }
}
