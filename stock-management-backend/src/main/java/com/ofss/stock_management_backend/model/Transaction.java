package com.ofss.stock_management_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    // Many transactions belong to one customer
    @ManyToOne
    @JoinColumn(name = "emailId", nullable = false) // matches Customer PK
    private Customer customer;

    // Many transactions involve one stock
    @ManyToOne
    @JoinColumn(name = "stockId", nullable = false) // matches Stocks PK
    private Stock stock;

    private int quantity;
    private double priceAtTransaction;

    @Enumerated(EnumType.STRING)
    private TransactionType type; // BUY or SELL

    private LocalDateTime timestamp;

    // --- Constructors ---
    public Transaction() {
        super();
    }

    public Transaction(Customer customer, Stock stock, int quantity, double priceAtTransaction, TransactionType type,
            LocalDateTime timestamp) {
        this.customer = customer;
        this.stock = stock;
        this.quantity = quantity;
        this.priceAtTransaction = priceAtTransaction;
        this.type = type;
        this.timestamp = timestamp;
    }

    // --- Getters & Setters ---
    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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

    public double getPriceAtTransaction() {
        return priceAtTransaction;
    }

    public void setPriceAtTransaction(double priceAtTransaction) {
        this.priceAtTransaction = priceAtTransaction;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
