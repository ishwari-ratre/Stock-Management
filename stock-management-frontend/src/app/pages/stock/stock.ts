
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Component, ElementRef, Input, OnInit, SimpleChanges, ViewChild } from '@angular/core';
import { ChartConfiguration, ChartOptions, ChartType } from 'chart.js';
import { forkJoin } from 'rxjs';
import { HistoryEntry, StockEntry } from '../../model/stock';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgChartsModule } from 'ng2-charts';
import { Notification } from '../../services/notification';
import { Chat } from '../chat/chat';

@Component({
  selector: 'app-stock',
  standalone:true,
  imports: [CommonModule,RouterModule, ReactiveFormsModule, FormsModule, NgChartsModule,HttpClientModule, Chat],
  templateUrl: './stock.html',
  styleUrl: './stock.css'
})
export class Stock implements OnInit {
  @ViewChild('chartSection') chartSection!: ElementRef;

  // Full and filtered stock lists
  allStocks: StockEntry[] = [];
  filteredStocks: StockEntry[] = [];

  // Filters
  industries: string[] = [];
  searchTerm: string = '';
  selectedIndustry: string = '';
  sortOrder: 'asc' | 'desc' = 'desc';

  // Selected stock & chart
  selectedStock: StockEntry | null = null;
  selectedRange: number = 30;

  chartData: ChartConfiguration<'line'>['data'] = {
    labels: [],
    datasets: [],
  };

  chartOptions: ChartOptions<'line'> = {
    responsive: true,
    plugins: {
      legend: {
        display: true,
        labels: { color: '#fff' }
      }
    },
    scales: {
      x: { ticks: { color: '#ccc' } },
      y: { ticks: { color: '#ccc' } }
    }
  };
  priceOrder: 'high' | 'low' | '' = '';


  constructor(
    private http: HttpClient,
    private notification: Notification
  ) { }

  ngOnInit(): void {
    this.fetchStocksWithHistory();
  }

  /**
   * Fetch stocks + their history to support chart view and filtering
   */
  fetchStocksWithHistory(): void {
    this.http.get<any[]>('http://localhost:3000/api/stocks').subscribe(stockList => {
      const historyCalls = stockList.map(s =>
        this.http.get<HistoryEntry[]>(`http://localhost:3000/api/stock/${s.symbol}/history`)
      );

      forkJoin(historyCalls).subscribe(histories => {
        this.allStocks = stockList.map((s, i) => {
          const h = histories[i].sort((a, b) =>
            new Date(a.tradeDate).getTime() - new Date(b.tradeDate).getTime()
          );

          const cp = h[h.length - 1]?.closePrice ?? s.basePrice;
          const ch = cp - s.basePrice;
          const chp = s.basePrice ? (ch / s.basePrice) * 100 : 0;

          return {
            symbol: s.symbol,
            name: s.name || s.Name,
            basePrice: s.basePrice,
            currentPrice: cp,
            change: ch,
            changePercent: chp,
            industry: s.industry || s.Industry,
            history: h
          };
        });

        this.filteredStocks = [...this.allStocks];
        this.industries = [...new Set(this.allStocks.map(s => s.industry))];
      });
    });
  }

  /**
   * Filters stock list based on search, industry and sort order
   */
  applyFilters(): void {
    this.filteredStocks = this.allStocks
      .filter(stock => {
        const searchMatch = !this.searchTerm ||
          stock.name.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
          stock.symbol.toLowerCase().includes(this.searchTerm.toLowerCase());

        const industryMatch = !this.selectedIndustry || stock.industry === this.selectedIndustry;

        return searchMatch && industryMatch;
      })
      .sort((a, b) => {
        if (this.priceOrder === 'high') return b.currentPrice - a.currentPrice;
        if (this.priceOrder === 'low') return a.currentPrice - b.currentPrice;
        return 0;
      });
  }

  /**
   * Change sorting (triggered via dropdown)
   */
  onSortChange(order: 'asc' | 'desc') {
    this.sortOrder = order;
    this.applyFilters(); // Reapply sorting
  }

  /**
   * When user clicks a stock card
   */
  selectStock(stock: StockEntry): void {
    this.selectedStock = stock;
    this.renderChart();
  }

  /**
   * Render the line chart based on selected stock + date range
   */
  renderChart(days = this.selectedRange): void {
    if (!this.selectedStock) return;

    const history = this.selectedStock.history.slice(-days);

    this.chartData = {
      labels: history.map(e => new Date(e.tradeDate).toLocaleDateString()),
      datasets: [{
        label: `${this.selectedStock.symbol} — Last ${days} days`,
        data: history.map(e => e.closePrice),
        fill: false,
        borderColor: '#D65A31',
        backgroundColor: '#D65A31',
        tension: 0.3,
        pointRadius: 4,
        pointHoverRadius: 6
      }]
    };

    setTimeout(() => {
      this.chartSection?.nativeElement.scrollIntoView({ behavior: 'smooth' });
    }, 100);
  }

  /**
   * Change date range (e.g. 7, 30, 90 days)
   */
  setRange(days: number): void {
    this.selectedRange = days;
    this.renderChart(days);
  }


  //logic for buy/sell

  // Popup flags
  showBuyPopup: boolean = false;
  showSellPopup: boolean = false;

  // Buy/Sell form fields
  buySymbol: string = '';
  buyQuantity: number | null = null;
  buyPrice: number | null = null;

  sellSymbol: string = '';
  sellQuantity: number | null = null;
  sellPrice: number | null = null;

  // Open popup
  openPopup(type: 'buy' | 'sell') {
    if (!this.selectedStock) return;

    if (type === 'buy') {
      this.buySymbol = this.selectedStock.symbol;
      this.buyPrice = this.selectedStock.currentPrice;
      this.showBuyPopup = true;
    } else {
      this.sellSymbol = this.selectedStock.symbol;
      this.sellPrice = this.selectedStock.currentPrice;
      this.showSellPopup = true;
    }
  }

  // Close popup
  closePopup(type: 'buy' | 'sell') {
    if (type === 'buy') this.showBuyPopup = false;
    else this.showSellPopup = false;
  }

// Confirm Buy
  confirmBuy() {
    if (!this.buyQuantity || this.buyQuantity <= 0) return alert('Enter valid quantity');

    const payload = {
      symbol: this.buySymbol,
      quantity: this.buyQuantity,
      email:''
    };

    // POST request to buy endpoint, JWT will be attached automatically via interceptor
    this.http.post('http://localhost:3000/api/transaction/buy', payload).subscribe({
      next: (res) => {
        this.notification.success("purchase complete")
        this.showBuyPopup = false;
      },
      error: (err) => {
        this.notification.error("problem purchasing stock")
        console.error('Buy failed:', err);
      }
    });
  }

  // Confirm Sell
  confirmSell() {
    if (!this.sellQuantity || this.sellQuantity <= 0) return alert('Enter valid quantity');

    const payload = {
      symbol: this.sellSymbol,
      quantity: this.sellQuantity,
      email:''
    };

    // POST request to sell endpoint, JWT will be attached automatically via interceptor
    this.http.post('http://localhost:3000/api/transaction/sell', payload).subscribe({
      next: (res) => {
        console.log('Sell success:', res);
        this.notification.success("stock sold")
        this.showSellPopup = false;
      },
      error: (err) => {
        this.notification.error("problem selling stock")
        console.error('Sell failed:', err);
      }
    });
  }

}
