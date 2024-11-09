import { CommonModule } from '@angular/common';
import { Component, Renderer2 } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { UserResponse } from '../responses/user/user.response';
import { UserService } from '../service/user.service';
import { TokenService } from '../service/token.service';
import { AuthService } from '../service/auth.service';
import { AuthDTO } from '../dtos/auth.dto';
import { SweetAlertService } from '../service/sweet-alert.service';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterOutlet],
  templateUrl: './admin.component.html',
})
export class AdminComponent {
  userResponse: UserResponse | undefined;
  constructor(
    private renderer: Renderer2,
    private router: Router,
    private userService: UserService,
    private tokenService: TokenService,
    private authService: AuthService,
    private alert: SweetAlertService
  ) {}

  ngOnInit(): void {
    this.loadScript('/assets/js/sb-admin-2.min.js');
    this.loadScript('/vendor/chart.js/Chart.min.js');
    this.loadScript('/assets/js/demo/chart-area-demo.js');
    this.loadScript('/assets/js/demo/chart-pie-demo.js');

    const token = localStorage.getItem('access_token');
    if (token) {
      this.userService.getUserDetail().subscribe({
        next: (response) => {
          this.userResponse = response.result;
        },
      });
    }
  }

  loadScript(src: string) {
    const script = this.renderer.createElement('script');
    script.src = src;
    script.type = 'text/javascript';
    script.async = true;
    this.renderer.appendChild(document.body, script);
  }
  homeNavigate() {
    this.router.navigate(['/']);
  }
  dashboardHome() {
    this.router.navigate(['/quan-ly/trang-chu']).then(() => {
      window.location.reload();
    });
  }
  logout() {
    this.alert.showConfirm('Cảnh báo','Xác nhận đăng xuất').then((result) => {
      if (result.isConfirmed) {
        var token = this.tokenService.getToken();
        if (token) {
          const data = new AuthDTO(token);
          this.authService.logout(data).subscribe({
            next: (response) => {
              localStorage.removeItem('access_token');
              this.router.navigate(['dang-nhap']);
              this.alert.showSuccess('Đăng xuất thành công');
            },
            error: (error) => {
              this.alert.showError('Đã xảy ra lỗi');
              console.log(error);
            },
          });
        } else {
          this.router.navigate(['dang-nhap']);
        }
      }
    });
  }
}
