import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { Notification } from './notification';
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:3000/api/auth'; // adjust if needed

  constructor(
    private http: HttpClient,
    private notification: Notification
  ) {}

  login(email:string, password:string): Observable<any> {
    const params = new HttpParams().set('email',email).set('password',password)
    return this.http.post(`${this.apiUrl}/login?email=${email}&password=${password}`,{params}, {responseType:'text'});
  }

  register(user: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, user);
  }

  saveToken(token: string) {
    localStorage.setItem('token', token);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  isLoggedIn(): boolean {
    const token = this.getToken();
    if(!token){
      return false;
    }

    try{
      const payload = JSON.parse(atob(token.split('.')[1]));
      const valid = payload.exp * 1000 > Date.now()
      return valid;
    }catch(e){
      console.error('invalid token', e);
      return false;
    }
  }

  logout() {
    localStorage.removeItem('token');
    this.notification.success("logged out successfully!")
  }
}
