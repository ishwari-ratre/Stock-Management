package com.ofss.stock_management_backend.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ofss.stock_management_backend.model.Stock;
import com.ofss.stock_management_backend.model.StockHistory;

public interface StockHistoryRepository extends JpaRepository<StockHistory, Long> {

    List<StockHistory> findByStockOrderByTradeDateAsc(Stock stock);

    StockHistory findTopByStockOrderByTradeDateDesc(Stock stock);

    List<StockHistory> findTop2ByStockOrderByTradeDateDesc(Stock stock);

    List<StockHistory> findByStockAndTradeDateBetween(Stock stock, Date start, Date end);

    void deleteByStock(Stock stock);
}
