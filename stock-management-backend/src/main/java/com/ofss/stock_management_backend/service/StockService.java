package com.ofss.stock_management_backend.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ofss.stock_management_backend.dto.StockDTO;
import com.ofss.stock_management_backend.model.Stock;
import com.ofss.stock_management_backend.model.StockHistory;
import com.ofss.stock_management_backend.repository.StockHistoryRepository;
import com.ofss.stock_management_backend.repository.StockRepository;

import org.springframework.dao.DataIntegrityViolationException;

@Service
public class StockService {

    @Autowired
    private StockRepository sr;

    @Autowired
    private StockHistoryRepository shr;
    @Autowired
    private StockRecomendation stockrec;

    public ResponseEntity<Object> viewAllStocks() {
        List<Stock> stocks = sr.findAll();

        for (Stock stock : stocks) {
            List<StockHistory> history = shr.findTop2ByStockOrderByTradeDateDesc(stock);
            if (history.size() >= 2) {
                double latest = history.get(0).getClosePrice();
                double previous = history.get(1).getClosePrice();
                stock.setColorTag(latest > previous ? "green" : "red");
            } else {
                stock.setColorTag("grey");
            }
        }

        return ResponseEntity.ok(stocks);
    }

    // public ResponseEntity<Object> addStock(Stock stock) {
    // // return ResponseEntity.status(201).body(sr.save(stock));
    // try {
    // Stock savedStock = sr.save(stock);
    // return ResponseEntity.status(HttpStatus.CREATED).body(savedStock);
    // } catch (Exception e) {
    // e.printStackTrace();
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    // .body("Error saving stock: " + e.getMessage());
    // }
    // }

    // updated one and working but at home check it once
    public ResponseEntity<Object> addStock(Stock stock) {
        try {
            // Check for existing stock by symbol
            Stock existing = sr.findBySymbol(stock.getSymbol());
            if (existing != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Stock with symbol '" + stock.getSymbol() + "' already exists.");
            }
            Stock savedStock = sr.save(stock);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedStock);

        } catch (DataIntegrityViolationException e) {
            // Handles DB constraint violations
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Duplicate symbol error: " + e.getMessage());

        } catch (Exception e) {
            // Handles any other unexpected errors
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving stock: " + e.getMessage());
        }
    }

    public ResponseEntity<Object> findBySymbol(String Symbol) {
        Stock stock = sr.findBySymbol(Symbol);
        if (stock == null) {
            // return ResponseEntity.notFound().build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Stock with symbol '" + Symbol + "' not found");
        }
        return ResponseEntity.ok(stock);
    }

    // added this
    public Stock getStockBySymbol(String symbol) {
        return sr.findBySymbol(symbol);
    }
   // 4. Get stocks sorted by latest price
    public ResponseEntity<Object> getAllStocksSortedByLatestPrice(String order) {
        List<Stock> stocks = sr.findAll();
        List<StockDTO> enrichedStocks = new ArrayList<>();

        for (Stock stock : stocks) {
            List<StockHistory> history = shr.findTop2ByStockOrderByTradeDateDesc(stock);
            if (history.isEmpty()) {
                continue;
            }

            StockHistory latest = history.get(0);
            StockHistory previous = history.size() > 1 ? history.get(1) : null;

            double latestPrice = latest.getClosePrice();
            String color = "grey";
            if (previous != null) {
                color = latestPrice > previous.getClosePrice() ? "green" : "red";
            }

            StockDTO dto = new StockDTO(
                    stock.getSymbol(),
                    stock.getName(),
                    stock.getIndustry(),
                    latestPrice,
                    color
            );

            enrichedStocks.add(dto);
        }

        enrichedStocks.sort((a, b) -> {
            double priceA = a.getLatestPrice() != null ? a.getLatestPrice() : 0;
            double priceB = b.getLatestPrice() != null ? b.getLatestPrice() : 0;
            return "desc".equalsIgnoreCase(order)
                    ? Double.compare(priceB, priceA)
                    : Double.compare(priceA, priceB);
        });

        return ResponseEntity.ok(enrichedStocks);
    }

    // 5. Find stocks by industry
    public ResponseEntity<Object> findByIndustry(String industry) {
        List<Stock> stocks = sr.findAllByIndustry(industry);
        return (stocks == null || stocks.isEmpty()) ? ResponseEntity.notFound().build() : ResponseEntity.ok(stocks);
    }

    // 6. Update stock
    public ResponseEntity<Object> updateStock(String symbol, Stock updatedStock) {
        Stock stock = sr.findBySymbol(symbol);
        if (stock == null) {
            return ResponseEntity.notFound().build();
        }

        stock.setName(updatedStock.getName());
        stock.setBasePrice(updatedStock.getBasePrice());
        stock.setIndustry(updatedStock.getIndustry());

        return ResponseEntity.ok(sr.save(stock));
    }

    // 7. Delete stock by symbol
    public ResponseEntity<Object> deleteBySymbol(String symbol) {
        Stock stock = sr.findBySymbol(symbol);
        if (stock == null) {
            return ResponseEntity.notFound().build();
        }

        sr.delete(stock);
        return ResponseEntity.ok("Stock deleted successfully.");
    }

    // 8. Get latest price
    public ResponseEntity<Object> getLatestPrice(String symbol) {
        Stock stock = sr.findBySymbol(symbol);
        if (stock == null) {
            return ResponseEntity.notFound().build();
        }

        StockHistory latest = shr.findTopByStockOrderByTradeDateDesc(stock);
        return ResponseEntity.ok(latest != null ? latest.getClosePrice() : "No history available.");
    }

    // 9. Get stock history
    public ResponseEntity<Object> getStockHistory(String symbol) {
        Stock stock = sr.findBySymbol(symbol);
        if (stock == null) {
            return ResponseEntity.notFound().build();
        }

        List<StockHistory> history = shr.findByStockOrderByTradeDateAsc(stock);
        return ResponseEntity.ok(history);
    }

    // 10. Average close price between dates
    public ResponseEntity<Object> getAverageClosePrice(String symbol, Date start, Date end) {
        Stock stock = sr.findBySymbol(symbol);
        if (stock == null) {
            return ResponseEntity.notFound().build();
        }

        List<StockHistory> history = shr.findByStockAndTradeDateBetween(stock, start, end);
        if (history.isEmpty()) {
            return ResponseEntity.ok("No data available for the given date range.");
        }

        double average = round(history.stream()
                .mapToDouble(StockHistory::getClosePrice)
                .average()
                .orElse(0.0));

        return ResponseEntity.ok(average);
    }

    // 11. Delete all history
    public ResponseEntity<Object> deleteAllHistoryForStock(String symbol) {
        Stock stock = sr.findBySymbol(symbol);
        if (stock == null) {
            return ResponseEntity.notFound().build();
        }

        shr.deleteByStock(stock);
        return ResponseEntity.ok("All history deleted for stock: " + symbol);
    }

    // 12. Top 5 movers by % change
    public ResponseEntity<Object> getTopMovers() {
        List<Stock> stocks = sr.findAll();
        List<Map<String, Object>> movers = new ArrayList<>();

        for (Stock stock : stocks) {
            List<StockHistory> historyList = shr.findTop2ByStockOrderByTradeDateDesc(stock);
            if (historyList.size() < 2) {
                continue;
            }

            double latest = historyList.get(0).getClosePrice();
            double previous = historyList.get(1).getClosePrice();
            double changePercent = ((latest - previous) / previous) * 100;

            Map<String, Object> map = new HashMap<>();
            map.put("symbol", stock.getSymbol());
            map.put("changePercent", round(changePercent));
            movers.add(map);
        }

        movers.sort((a, b) -> Double.compare((Double) b.get("changePercent"), (Double) a.get("changePercent")));
        return ResponseEntity.ok(movers.subList(0, Math.min(5, movers.size())));
    }

    // 13. Chat-style smart recommendation
    public ResponseEntity<Object> chatrec(String message, Double budget, String risk) {
        if (message == null || message.isBlank()) {
            return ResponseEntity.badRequest().body("Message cannot be null or empty");
        }

        message = message.toLowerCase();

        if (message.contains("recommend") || message.contains("buy")) {
            List<Map<String, Object>> recommendations
                    = (List<Map<String, Object>>) stockrec.getSmartRecommendations(risk).getBody();

            if (budget == null) {
                return ResponseEntity.ok(recommendations);
            }

            double remaining = budget;

            List<Map<String, Object>> affordable = recommendations.stream()
                    .filter(r -> {
                        Object priceObj = r.get("latestPrice");
                        return priceObj instanceof Number && ((Number) priceObj).doubleValue() <= budget;
                    }).collect(Collectors.toList());

            List<Map<String, Object>> strongBuys = affordable.stream()
                    .filter(r -> "Strong Buy".equalsIgnoreCase((String) r.get("recommendation")))
                    .sorted(Comparator.comparingDouble(r -> ((Number) r.get("latestPrice")).doubleValue()))
                    .collect(Collectors.toList());

            List<Map<String, Object>> buys = affordable.stream()
                    .filter(r -> "Buy".equalsIgnoreCase((String) r.get("recommendation")))
                    .sorted(Comparator.comparingDouble(r -> ((Number) r.get("latestPrice")).doubleValue()))
                    .collect(Collectors.toList());

            List<Map<String, Object>> selected = new ArrayList<>();
            remaining = buyStocksWithinBudget(strongBuys, remaining, selected);
            if (remaining > 0) {
                buyStocksWithinBudget(buys, remaining, selected);
            }

            return ResponseEntity.ok(selected);
        }

        return ResponseEntity.ok("Sorry, I can only help with stock recommendations currently.");
    }

    private double buyStocksWithinBudget(List<Map<String, Object>> stocks, double budget, List<Map<String, Object>> bought) {
        Iterator<Map<String, Object>> it = stocks.iterator();
        while (it.hasNext()) {
            Map<String, Object> stock = it.next();
            double price = ((Number) stock.get("latestPrice")).doubleValue();

            if (price <= budget) {
                bought.add(stock);
                budget -= price;
                it.remove();
            } else {
                break;
            }
        }
        return budget;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}