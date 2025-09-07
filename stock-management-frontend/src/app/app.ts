import { Component, signal } from '@angular/core';
import { RouterModule, RouterOutlet } from '@angular/router';
import { NotificationComponent } from './pages/notification/notification';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { JwtInterceptor } from './interceptors/jwt';

@Component({
  selector: 'app-root',
  templateUrl: './app.html',
  standalone: true,
  styleUrl: './app.css',
  imports: [RouterModule, NotificationComponent,RouterOutlet],
  providers:[
    {provide:HTTP_INTERCEPTORS, useClass:JwtInterceptor,multi:true}
  ],
})
export class App {
  protected readonly title = signal('stock-management-frontend');
}
