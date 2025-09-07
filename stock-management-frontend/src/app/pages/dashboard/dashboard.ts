import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { Notification } from '../../services/notification';

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
  portfolioValue = 0;
  portfolioInvested = 0;
  portfolioGainLoss = 0;
  transactions: any[] = [];
  marketList: any[] = [];
  profile = { email: '',name:'', city: '', phoneNumber: 0 };
  showModal = false;
  loading = false;
  errorMessage = '';
  constructor(
    private http: HttpClient,
    private notification: Notification,
  ) {}

  ngOnInit() {
    this.fetchDashboard();
  }

  fetchDashboard() {
    this.loading = true;
    this.errorMessage = '';
    const token = localStorage.getItem('token')
    console.log(token);
    this.http.get<any>('http://localhost:3000/api/dashboard/details').subscribe({
      next: (res) => {
        this.username = res.customer.firstName;
        this.walletBalance = res.walletBalance ?? 0;
        this.portfolioValue = res.portfolioValue ?? 0;
        this.portfolioInvested = res.invested ?? 0;
        this.portfolioGainLoss = res.profitLoss ?? 0;
        this.transactions = res.lastTransactions ?? [];
        this.marketList = res.market ?? [];
        this.profile = {
          email: res.customer.emailId,
          name: `${res.customer.firstName} ${res.customer.lastName}`,
          city: res.customer.city,
          phoneNumber: res.customer.phoneNumber,
        };

        this.loading = false;
      },
      error: (err) => {
        this.notification.error(err);
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
        this.notification.success("profile updated")
        this.username = this.profile.name.split(' ')[0];
        this.closeModal();
      },
      error: (err) => {
        this.notification.error("could not update profile details")
        console.error('Profile update failed', err);
        alert(err.error?.message || 'Could not update profile.');
      },
    });
  }
}
