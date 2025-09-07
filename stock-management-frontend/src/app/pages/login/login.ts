import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule, UrlSegment } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-login',
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class Login implements OnInit {
  isLoginMode: boolean = true;
  error = '';
  baseUrl = 'http://localhost:3000/api/auth'

  loginData = { email: '', password: '' };
  registerData = { name: '', email: '', password: '', city: '', phone: '' };

  constructor(private http:HttpClient,private route: ActivatedRoute, private router:Router){}
  ngOnInit(): void {
      this.route.url.subscribe(urlSegments=>{
        const currentPath = urlSegments[0]?.path;
        if(currentPath === 'register'){
          this.isLoginMode = false;
        }else{
          this.isLoginMode = true;
        }
      })
  }

  showLoginForm() {
    this.isLoginMode = true;
  }

  showRegisterForm() {
    this.isLoginMode = false;
  }

  onLogin() {
    this.error = '';
    const body = { email: this.loginData.email, password: this.loginData.password };

    this.http.post(`${this.baseUrl}/login?email=${this.loginData.email}&password=${this.loginData.password}`,{}, { responseType: 'text' }).subscribe({
      next: (res: string) => {
        const prefix = 'Login successful! Your JWT token: ';
        if (typeof res === 'string' && res.startsWith(prefix)) {
          const token = res.substring(prefix.length).trim();
          localStorage.setItem('token', token); // save token (Auth service reads same key)
          alert('logged in');
          this.router.navigate(['/']);
        } else {
          this.error = 'Unexpected login response from server.';
          console.error('Login response:', res);
        }
      },
      error: (err) => {
        console.error('Login error', err);
        // show backend message if present, otherwise generic
        alert(err);
        this.error = (err?.error && typeof err.error === 'string') ? err.error : 'Invalid credentials';
      }
    });
  }

  onRegister() {
    const [firstName, ...rest] = this.registerData.name.trim().split(' ');
    const lastName = rest.join(' ');
    const body = {
      firstName,
      lastName,
      emailId:this.registerData.email,
      password:this.registerData.password,
      city:this.registerData.city,
      phoneNo:this.registerData.phone
    };

    this.http.post('http://localhost:3000/api/auth/register', body, { responseType: 'text' })
      .subscribe({
        next: (res) => {
          console.log('Registration successful:', res);
          alert('Registration successful! You can now login.');
          this.showLoginForm() // Switch to login form after register
        },
        error: (err) => {
          console.error('Registration failed:', err);
          alert(err);
          this.error = 'Registration failed. Try again.';
        }
      });
  }

}
