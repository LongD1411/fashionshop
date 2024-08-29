import { inject, Injectable } from '@angular/core';
import { TokenService } from '../service/token.service';
import { UserService } from '../service/user.service';
import {
  ActivatedRouteSnapshot,
  CanActivateFn,
  Router,
  RouterStateSnapshot,
} from '@angular/router';
import { AuthService } from '../service/auth.service';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard {
  constructor(private authService: AuthService, private router: Router) {}
  canActive(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const isTokenExpired = this.authService.isTokenValidate();
    const isUserValid = this.authService.isUserValidate();
    if (!isTokenExpired && isUserValid) {
      return true;
    } else {
      this.router.navigate(['/dang-nhap']);
      return false;
    }
  }
}
export const AuthGuardFn: CanActivateFn = (
  next: ActivatedRouteSnapshot,
  state: RouterStateSnapshot
) => {
  return inject(AuthGuard).canActive(next, state);
};
