package com.ofss.stock_management_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ofss.stock_management_backend.model.Stock;

@Repository
public interface StocksRepository extends JpaRepository<Stock, Integer> {
    // Example custom finder
    Stock findByStockSymbol(String stockSymbol);
}
