import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { UserService } from '../../service/user.service';
import { LoginDTO } from '../../dtos/user/login.dto';
import { TokenService } from '../../service/token.service';
import { AuthResponse } from '../../responses/auth.response';
import { Router, RouterLink } from '@angular/router';
import { SweetAlertService } from '../../service/sweet-alert.service';
import { AuthService } from '../../service/auth.service';
import { BaseResponse } from '../../responses/base.response';
import { AuthDTO } from '../../dtos/auth.dto';
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  loginForm: FormGroup;
  registrationMessage: string = '';
  isError: boolean = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private tokenService: TokenService,
    private router: Router,
    private alert: SweetAlertService
  ) {
    this.loginForm = this.fb.group({
      phone: ['', [Validators.required, Validators.minLength(6)]],
      password: ['', [Validators.required, Validators.minLength(5)]],
    });
  }
  submitHandler() {
    if (this.loginForm.valid) {
      const loginData: LoginDTO = {
        phone_number: this.loginForm.get('phone')?.value,
        password: this.loginForm.get('password')?.value,
      };
      this.authService.login(loginData).subscribe({
        next: (response) => {
          const token = response.result.token;
          this.tokenService.setToken(token);
          const payload = JSON.parse(atob(token.split('.')[1]));
          if (payload.scope && payload.scope.includes('ROLE_ADMIN')) {
            this.router.navigate(['/quan-ly']);
          } else {
            this.router.navigate(['/']);
          }
          this.alert.showSuccess('Đăng nhập thành công');
        },
        error: (response) => {
          this.alert.showError(response.error.message);
          this.isError = true;
        },
        complete: () => {},
      });

    } else {
      this.registrationMessage = 'Điền đúng định dạng các trường.';
      this.isError = true;
    }
  }
  get phone() {
    return this.loginForm.get('phone')!;
  }
  get password() {
    return this.loginForm.get('password')!;
  }
  homeNavigate() {
    this.router.navigate(['/']);
  }
}
