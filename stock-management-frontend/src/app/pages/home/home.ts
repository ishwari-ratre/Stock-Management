import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { AuthService } from '../../services/auth';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [CommonModule, RouterModule],
  templateUrl: './home.html',
  styleUrl: './home.css',
  standalone:true
})
export class Home {
  constructor(public authService:AuthService, private router:Router){}
  logout(){
    this.authService.logout();
    this.router.navigate(['/']);
  }
}
