import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AuthService } from '../services/auth';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const isLoggedIn = this.authService.isLoggedIn();

    if (isLoggedIn) {
      // If logged in and accessing root `/`, redirect → `/dashboard`
      if (state.url === '/' || state.url === '') {
        this.router.navigate(['/dashboard']);
        return false;
      }
      return true;
    } else {
      // If not logged in and trying to access `/dashboard`, redirect → `/login`
      if (state.url.startsWith('/dashboard')) {
        this.router.navigate(['/login']);
        return false;
      }
      // Allow access to `/` (landing page) even when not logged in
      return true;
    }
  }
}
