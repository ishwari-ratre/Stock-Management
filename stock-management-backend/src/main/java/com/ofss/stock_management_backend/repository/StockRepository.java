package com.ofss.stock_management_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ofss.stock_management_backend.model.Stock;

public interface StockRepository extends JpaRepository<Stock, Integer> {

    Stock findBySymbol(String Symbol);

    List<Stock> findAllByIndustry(String industry);

}