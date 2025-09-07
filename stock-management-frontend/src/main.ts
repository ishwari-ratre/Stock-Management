import { bootstrapApplication } from '@angular/platform-browser';
import { App } from './app/app';
import { provideHttpClient, HTTP_INTERCEPTORS } from '@angular/common/http';
import { JwtInterceptor } from './app/interceptors/jwt';
import { provideRouter, Routes } from '@angular/router';
import { Home } from './app/pages/home/home';
import { Login } from './app/pages/login/login';
import { NotificationComponent } from './app/pages/notification/notification';
import { Dashboard } from './app/pages/dashboard/dashboard';
import { AuthGuard } from './app/guards/guard';
import { Transaction } from './app/pages/transaction/transaction';
import { Stock } from './app/pages/stock/stock';
import { Portfolio } from './app/pages/portfolio/portfolio';

const routes: Routes = [
    {
      path: '',
      component: Home,
      children:[
        {path:'',component:Home, canActivate:[AuthGuard]},
        {path:'dashboard',component:Dashboard, canActivate:[AuthGuard]},
        {path:'transaction',component:Transaction, canActivate:[AuthGuard]},
        {path:'stock',component:Stock, canActivate:[AuthGuard]},
        {path:'portfolio',component:Portfolio, canActivate:[AuthGuard]},
      ],
    },
  { path: 'login', component: Login },
  {path:'register',component:Login},
];

bootstrapApplication(App, {
  providers: [
    provideHttpClient(),
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    provideRouter(routes)
  ]
}).catch(err => console.error(err));
