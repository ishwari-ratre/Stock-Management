import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Notification } from '../../services/notification';

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
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './transaction.html',
  styleUrls: ['./transaction.css']
})
export class Transaction implements OnInit {

  transactions: TransactionEntry[] = [];
  filteredTransactions: TransactionEntry[] = [];

  filterType: string = 'all';
  searchStock: string = '';

  constructor(
    private http: HttpClient,
    private notification: Notification
  ) {}

  ngOnInit(): void {
    this.fetchTransactions();
  }

  fetchTransactions(): void {
    this.http.get<any[]>('http://localhost:3000/api/transaction/history')
      .subscribe({
        next: (data) => {
          // Map backend response to frontend model
          this.transactions = data.map(txn => ({
            id: txn.transactionId,
            name: txn.stock.name,
            symbol: txn.stock.symbol,
            quantity: txn.quantity,
            price: txn.priceAtTransaction || txn.stock.basePrice, // fallback if priceAtTransaction is 0
            type: txn.type.toLowerCase() === 'buy' ? 'buy' : 'sell',
            timestamp: new Date(txn.timestamp).toLocaleString()
          }));
          this.applyFilters();
        },
        error: (err) => {
          this.notification.error("Cannot fetch transaction data");
          console.error('Error fetching transactions:', err);
        }
      });
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
