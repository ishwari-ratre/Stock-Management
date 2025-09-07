import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {Home} from './pages/home/home';
import { Login } from './pages/login/login';
import { Dashboard } from './pages/dashboard/dashboard';
import { AuthGuard } from './guards/guard';
import { Transaction } from './pages/transaction/transaction';
import { Stock } from './pages/stock/stock';
import { Portfolio } from './pages/portfolio/portfolio';

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
  { path: 'register', component: Login },
  {path: 'login', component:Login},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
