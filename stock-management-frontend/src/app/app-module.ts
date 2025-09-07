import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { provideHttpClient, HTTP_INTERCEPTORS } from '@angular/common/http';
import { JwtInterceptor } from './interceptors/jwt';
// Standalone components
import { Home } from './pages/home/home';
import { Login } from './pages/login/login';
import { Transaction } from './pages/transaction/transaction';
import { Stock } from './pages/stock/stock';
import { Chat } from './pages/chat/chat';
import { Portfolio } from './pages/portfolio/portfolio';

@NgModule({
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    Home,
    Login,
    Notification,
    Transaction,
    Stock,
    Chat
  ],
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideHttpClient(),
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
  ],
  bootstrap: [App],
  declarations: [
    Transaction,
    Stock,
    Portfolio
  ]
})
export class AppModule { }
