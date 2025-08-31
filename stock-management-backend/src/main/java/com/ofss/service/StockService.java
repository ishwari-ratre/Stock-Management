package com.ofss.service;

import com.ofss.model.Stocks;
import com.ofss.repository.StocksRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    @Autowired
    private StocksRepository stockRepository;

    public List<Stocks> getAllStocks() {
        return stockRepository.findAll();
    }

    public Stocks getStockById(int id) {
        return stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock not found with id: " + id));
    }

    public Stocks getStockBySymbol(String symbol) {
        return stockRepository.findByStockSymbol(symbol);
    }

    public Stocks saveStock(Stocks stock) {
        return stockRepository.save(stock);
    }

    public void deleteStock(int id) {
        stockRepository.deleteById(id);
    }
}

