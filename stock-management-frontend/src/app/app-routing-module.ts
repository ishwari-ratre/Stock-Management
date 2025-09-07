import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {Home} from './pages/home/home';
import { Login } from './pages/login/login';

const routes: Routes = [
  {path: '', component: Home},
  { path: 'register', component: Login },
  {path: 'login', component:Login},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
