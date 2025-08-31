package com.ofss.repository;

import com.ofss.model.Stocks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StocksRepository extends JpaRepository<Stocks, Integer> {
    // Example custom finder
    Stocks findByStockSymbol(String stockSymbol);
}
