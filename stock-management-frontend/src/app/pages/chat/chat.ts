import { Component, EventEmitter, Output } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { StockEntry } from '../../model/stock';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
// import {NgChartsModule} from 'ng2-charts';
type Message = {
  from: 'user' | 'bot';
  content: string | { [category: string]: StockEntry[] };
};

@Component({
  selector: 'app-chat',
  standalone: true,
  imports:[CommonModule, RouterModule, FormsModule,ReactiveFormsModule],
  templateUrl: './chat.html',
  styleUrls: ['./chat.css']
}) export class Chat {
  objectKeys = Object.keys;
  isOpen = false;
  isLoading = false;
  userMessage = '';
  messages: { from: 'user' | 'bot', content: string | { [category: string]: StockEntry[] } }[] = [];
  @Output() stockClicked = new EventEmitter<StockEntry>();

  constructor(private http: HttpClient) { }

  toggleChat() {
    this.isOpen = !this.isOpen;
  }

  sendMessage() {
    const trimmedMessage = this.userMessage.trim();
    if (!trimmedMessage) return;

    this.messages.push({ from: 'user', content: trimmedMessage });
    this.userMessage = '';
    this.isLoading = true;

    const budgetMatch = trimmedMessage.match(/(\d{1,3}(?:,\d{3})*(?:\.\d+)?|\d+(?:\.\d+)?)/);
    let budget = 100.0;

    if (budgetMatch) {
      try {
        budget = parseFloat(budgetMatch[0].replace(/,/g, ''));
      } catch (e) {
        console.warn('Failed to parse budget:', e);
      }
    }

    const payload = {
      message: trimmedMessage,
      budget: Number(budget.toFixed(2)),
      riskProfile: 'medium'
    };

    this.http.post<StockEntry[]>('http://localhost:3000/api/chat/recommend', payload).subscribe({
      next: (stocks) => {
        this.isLoading = false;

        if (!stocks.length) {
          this.pushBotText('No recommendations available.');
          return;
        }

        const grouped: { [category: string]: StockEntry[] } = {};

        stocks.forEach((stock) => {
          const category = stock.recommendation || 'Uncategorized';
          if (!grouped[category]) grouped[category] = [];

          grouped[category].push(stock);

        });

        // Mimic typing delay
        setTimeout(() => {
          this.messages.push({ from: 'bot', content: grouped });
        }, 1000);
      },
      error: () => {
        this.isLoading = false;
        this.pushBotText('Error fetching recommendations. Please try again.');
      }
    });
  }

  isGroupedStock(val: any): val is { [category: string]: StockEntry[] } {
    return val && typeof val === 'object' && !Array.isArray(val);
  }

  getIcon(category: string): string {
    switch (category.toLowerCase()) {
      case 'strong buy':
        return '🟢';
      case 'buy':
        return '🟡';
      case 'watchlist':
        return '🔵';
      case 'avoid':
        return '🔴';
      default:
        return '⚪';
    }
  }

  // Utility to push text as if typed
  pushBotText(text: string) {
    this.isLoading = false;
    let index = 0;
    let displayed = '';

    const interval = setInterval(() => {
      if (index < text.length) {
        displayed += text[index];
        if (index === 0) {
          this.messages.push({ from: 'bot', content: displayed });
        } else {
          const lastMsg = this.messages[this.messages.length - 1];
          if (typeof lastMsg.content === 'string') {
            lastMsg.content = displayed;
          }
        }
        index++;
      } else {
        clearInterval(interval);
      }
    }, 30); // adjust typing speed here
  }
  selectStock(stock: StockEntry) {
    this.stockClicked.emit(stock);
  }
}