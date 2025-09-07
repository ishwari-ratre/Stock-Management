import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';

interface Stock {
  name: string;
  symbol: string;
  quantity: number;
  price: number;
  totalValue: number;
  profit: number;
}

@Component({
  selector: 'app-portfolio',
  templateUrl: './portfolio.html',
  styleUrls: ['./portfolio.css'],
  standalone:true,
  imports: [CommonModule, FormsModule, HttpClientModule,]
})
export class Portfolio implements OnInit {
  portfolio: Stock[] = [
    { name: "Apple Inc", symbol: "AAPL", quantity: 10, price: 180, totalValue: 1800, profit: 200 },
    { name: "Tesla", symbol: "TSLA", quantity: 5, price: 250, totalValue: 1250, profit: -50 },
    { name: "Amazon", symbol: "AMZN", quantity: 3, price: 3200, totalValue: 9600, profit: 500 },
    { name: "Google", symbol: "GOOGL", quantity: 8, price: 2800, totalValue: 22400, profit: 1200 }
  ];

  totalValue: number = 0;
  totalProfit: number = 0;

  ngOnInit(): void {
    this.calculateSummary();
  }

  calculateSummary(): void {
    this.totalValue = this.portfolio.reduce((sum, stock) => sum + stock.totalValue, 0);
    this.totalProfit = this.portfolio.reduce((sum, stock) => sum + stock.profit, 0);
  }
}
