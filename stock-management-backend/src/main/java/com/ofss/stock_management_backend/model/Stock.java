package com.ofss.stock_management_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "Stock")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stock_seq")
    @SequenceGenerator(name = "stock_seq", sequenceName = "STOCK_SEQ", allocationSize = 1)
    private int stockId;
    private String Name;
    @Column(name = "symbol", unique = true, nullable = false)
    private String symbol;
    private double basePrice;
    private String industry;
    // private double quantity;

    @Transient
    private String colorTag;

    public String getColorTag() {
        return colorTag;
    }

    public void setColorTag(String colorTag) {
        this.colorTag = colorTag;
    }

    public Stock() {
        super();
    }

    public Stock(int stockId,
            String Name,
            String symbol,
            double basePrice,
            String industry) {
        this.stockId = stockId;
        this.Name = Name;
        this.symbol = symbol;
        this.industry = industry;
    }

    public int getstockId() {
        return stockId;
    }

    public void setstockId(int stockId) {
        this.stockId = stockId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    // public double getQuantity() {
    // return quantity;
    // }
    // public void setQuantity(double quantity) {
    // this.quantity = quantity;
    // }
}