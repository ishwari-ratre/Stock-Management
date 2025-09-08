import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Notification } from '../../services/notification';

interface DashboardTransaction {
  transactionId: number;
  stockName: string;
  symbol: string;
  quantity: number;
  price: number;
  totalValue: number;
  timestamp: string;
  type: string;
  priceAtTransaction:number;

}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css'],
})
export class Dashboard implements OnInit {
  username = '';
  walletBalance = 0;
  portfolioInvested = 0;
  portfolioGainLoss = 0;
  transactions: DashboardTransaction[] = [];
  marketList: any[] = [];
  profile = { email: '', name: '', city: '', phoneNumber: 0 };
  showModal = false;
  loading = false;
  errorMessage = '';

  constructor(private http: HttpClient, private notification: Notification) {}

  ngOnInit() {
    this.fetchDashboard();
  }

  fetchDashboard() {
    this.loading = true;
    this.errorMessage = '';

    this.http.get<any>('http://localhost:3000/api/dashboard/details').subscribe({
      next: (res) => {
        // this.username = res.customer.firstName;
        // this.username = `${res.customer.firstName}`.trim();

        this.walletBalance = res.walletBalance ?? 0;
        this.portfolioInvested = res.invested ?? 0;
        this.portfolioGainLoss = res.profitLoss ?? 0;

        // Map API transactions to frontend-friendly structure
        this.transactions = (res.lastTransactions ?? []).map((txn: any) => ({
          transactionId: txn.transactionId,
          stockName: txn.stock.name,
          symbol: txn.stock.symbol,
          quantity: txn.quantity,
          price: txn.priceAtTransaction,
          totalValue: txn.quantity * (txn.priceAtTransaction || txn.stock.basePrice),
          timestamp: txn.timestamp,
          type:txn.type,
        }));

        // this.marketList = res.market ?? [];
       this.marketList = [
  { name: 'NIFTY 50', value: 19750.85 },
  { name: 'SENSEX', value: 66255.71 },
  { name: 'NASDAQ', value: 13290.78 },
  { name: 'DOW JONES', value: 34879.45 },
  { name: 'S&P 500', value: 4497.63 }
];
        this.profile = {
          email: res.customer.emailId,
          name: `${res.customer.firstName} ${res.customer.lastName ?? ''}`.trim(),
          city: res.customer.city,
          phoneNumber: res.customer.phoneNumber,
        };

        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
        this.notification.error(err);
        console.error('Error fetching dashboard details', err);
      },
    });
  }

  openModal() {
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
  }

  saveProfile() {
    this.http.patch('http://localhost:3000/api/customers/update', this.profile).subscribe({
      next: () => {
        this.notification.success('Profile updated');
        this.username = this.profile.name.split(' ')[0];
        this.closeModal();
      },
      error: (err) => {
        this.notification.error('Could not update profile details');
        console.error('Profile update failed', err);
      },
    });
  }
}
