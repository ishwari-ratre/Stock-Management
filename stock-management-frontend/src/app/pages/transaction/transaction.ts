import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

interface TransactionEntry {
  id: string;
  name: string;
  symbol: string;
  quantity: number;
  price: number;
  type: 'buy' | 'sell';
  timestamp: string;
}

@Component({
  selector: 'app-transaction',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './transaction.html',
  styleUrls: ['./transaction.css']
})
export class Transaction implements OnInit {

  transactions: TransactionEntry[] = [
    { id: "TXN001", name: "Apple Inc", symbol: "AAPL", quantity: 10, price: 180, type: "buy", timestamp: "2025-09-01 10:23" },
    { id: "TXN002", name: "Tesla", symbol: "TSLA", quantity: 5, price: 250, type: "sell", timestamp: "2025-09-02 12:45" },
    { id: "TXN003", name: "Amazon", symbol: "AMZN", quantity: 3, price: 3200, type: "buy", timestamp: "2025-09-03 09:15" },
    { id: "TXN004", name: "Google", symbol: "GOOGL", quantity: 8, price: 2800, type: "sell", timestamp: "2025-09-04 14:05" }
  ];

  filteredTransactions: TransactionEntry[] = [];

  filterType: string = 'all';
  searchStock: string = '';

  ngOnInit(): void {
    this.applyFilters();
  }

  applyFilters(): void {
    let filtered = [...this.transactions];

    if (this.filterType !== 'all') {
      filtered = filtered.filter(txn => txn.type === this.filterType);
    }

    if (this.searchStock.trim() !== '') {
      const search = this.searchStock.toLowerCase();
      filtered = filtered.filter(txn =>
        txn.name.toLowerCase().includes(search) || txn.symbol.toLowerCase().includes(search)
      );
    }

    this.filteredTransactions = filtered;
  }

  getTotalBuy(): number {
    return this.filteredTransactions.filter(txn => txn.type === 'buy').length;
  }

  getTotalSell(): number {
    return this.filteredTransactions.filter(txn => txn.type === 'sell').length;
  }
}
