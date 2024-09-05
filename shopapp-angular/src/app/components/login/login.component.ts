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
import { LoginResponse } from '../../responses/user/login.response';
import { Router, RouterLink } from '@angular/router';
import { SweetAlertService } from '../../service/sweet-alert.service';
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
    private userService: UserService,
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
      this.userService.login(loginData).subscribe({
        next: (response: LoginResponse) => {
          this.registrationMessage = response.message;
          const { token } = response;
          this.tokenService.setToken(token);
          this.router.navigate(['/']);
          this.alert.showSuccess('Đăng nhập thành công');
        },
        error: (error: any) => {
          this.alert.showError(error.error.message);
          this.isError = true;
        },
        complete: () => {},
      });
    } else {
      this.registrationMessage = 'Please fill all required fields correctly.';
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
