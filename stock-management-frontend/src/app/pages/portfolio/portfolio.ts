import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Notification } from '../../services/notification';

interface PortfolioResponse {
  stock: {
    stockId: number;
    symbol: string;
    basePrice: number;
    industry: string;
    colorTag: string | null;
    name: string;
  };
  quantity: number;
  currentPrice: number;
  avgPriceAtTransaction: number;
  totalValue: number;
  profitLoss: number;
}

interface StockView {
  name: string;
  symbol: string;
  quantity: number;
  price: number;       // current price
  totalValue: number;  // quantity * price
  profit: number;      // profit/loss
}

@Component({
  selector: 'app-portfolio',
  templateUrl: './portfolio.html',
  styleUrls: ['./portfolio.css'],
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule]
})
export class Portfolio implements OnInit {
  portfolio: StockView[] = [];
  totalValue: number = 0;
  totalProfit: number = 0;

  constructor(
    private http: HttpClient,
    private notification: Notification
  ) {}

  ngOnInit(): void {
    this.fetchPortfolio();
  }

  fetchPortfolio(): void {
    this.http.get<PortfolioResponse[]>('http://localhost:3000/api/transaction/portfolio')
      .subscribe({
        next: (data) => {
          // map backend response to frontend StockView
          this.portfolio = data.map(item => ({
            name: item.stock.name,
            symbol: item.stock.symbol,
            quantity: item.quantity,
            price: item.currentPrice,
            totalValue: item.totalValue,
            profit: item.profitLoss
          }));
          this.calculateSummary();
        },
        error: (err) => {
          this.notification.error("could not fetch data");
          console.error('Error fetching portfolio:', err);
        }
      });
  }

  calculateSummary(): void {
    this.totalValue = this.portfolio.reduce((sum, stock) => sum + stock.totalValue, 0);
    this.totalProfit = this.portfolio.reduce((sum, stock) => sum + stock.profit, 0);
  }
}
