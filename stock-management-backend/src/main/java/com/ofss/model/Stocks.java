package com.ofss.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="Stocks")
public class Stocks {
	@Id
	private int stockId;
	private String stockName;
	private String stockSymbol;
	private double stockPrice;
	private int stockVolume;
	private String exchangeName;
	
	public Stocks() {
		super();
	}

	public Stocks(int stockId, String stockName,String stockSymbol, double stockPrice,int stockVolume, String exchangeName) {
		this.stockId = stockId;
		this.stockName = stockName;
		this.stockSymbol=stockSymbol;
		this.stockPrice = stockPrice;
		this.exchangeName = exchangeName;
		this.stockVolume= stockVolume;
	}

	public int getStockId() {
		return stockId;
	}

	public void setStockId(int stockId) {
		this.stockId = stockId;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public String getStockSymbol() {
		return stockSymbol;
	}

	public void setStockSymbol(String stockSymbol) {
		this.stockSymbol=stockSymbol;
	}

	public double getStockPrice() {
		return stockPrice;
	}

	public void setStockPrice(double stockPrice) {
		this.stockPrice = stockPrice;
	}

	public String getExchangeName() {
		return exchangeName;
	}

	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}
	
	public void setStockVolume(int stockVolume) {
		this.stockVolume = stockVolume;
	}
	public int getStockVolume() {
		return stockVolume;
	}
}

