import { Component, OnInit } from '@angular/core';
import { enviroment } from '../../enviroments/enviroment';
import { ProductService } from '../../service/product.service';
import { ProductResponse } from '../../responses/product/product.response';
import { forkJoin, max } from 'rxjs';
import { CommonModule } from '@angular/common';
import { CategoryService } from '../../service/category.service';
import { CategoryResponse } from '../../responses/category/category.respones';
import { BannerResponse } from '../../responses/banner.respose';
import { Router, RouterLink } from '@angular/router';
import { CartService } from '../../service/cart.service';
import { UserService } from '../../service/user.service';
import { UserResponse } from '../../responses/user/user.response';
import { SweetAlertService } from '../../service/sweet-alert.service';
import { CurrencyService } from '../../service/currency.service';
import { AuthService } from '../../service/auth.service';
import { TokenService } from '../../service/token.service';
import { AuthDTO } from '../../dtos/auth.dto';
@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './home.component.html',
})
export class HomeComponent {
  categories: CategoryResponse[] = [];
  banners: BannerResponse[] = [];
  top8ProductUpdated: ProductResponse[] = [];
  userResponse: UserResponse | undefined;
  constructor(
    private categoryService: CategoryService,
    private productService: ProductService,
    private router: Router,
    private userService: UserService,
    private alert: SweetAlertService,
    public currency: CurrencyService,
    private authService: AuthService,
    private tokenService: TokenService
  ) {}
  ngOnInit(): void {
    forkJoin([
      this.categoryService.getCategories(),
      this.categoryService.getAllBanners(),
      this.productService.getTop8ProductUpdated(),
    ]).subscribe({
      next: ([categoriesResponse, bannersResponse, productResponse]) => {
        //1
        this.categories = categoriesResponse.results;
        this.categories.map((category) => {
          if (category.thumbnail) {
            category.thumbnail = `${enviroment.apiImage}/${category.thumbnail}`;
          }
          return category;
        });
        console.log(this.categories);
        //2
        this.banners = bannersResponse.results;
        this.banners.map((banner) => {
          banner.thumbnail = `${enviroment.apiImage}/${banner.thumbnail}`;
          return banner;
        });
        console.log(this.banners);
        //3
        this.top8ProductUpdated = productResponse.results;
        this.top8ProductUpdated.map((product) => {
          product.thumbnail = `${enviroment.apiImage}/${product.thumbnail}`;
        });
      },
      error: (error) => {
        console.log(error);
      },
      complete: () => {},
    });
    const token = localStorage.getItem('access_token');
    if (token) {
      this.userService.getUserDetail().subscribe({
        next: (response) => {
          this.userResponse = response.result;
        },
      });
    }
  }
  viewProductDetails(productId: number) {
    this.router.navigate(['/chi-tiet-san-pham'], {
      queryParams: { id: productId },
    });
  }
  logout() {
    this.alert.showConfirm('Cảnh báo', 'Xác nhận đăng xuất').then((result) => {
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
  viewAllOrders() {
    this.router.navigate(['/thong-tin-ca-nhan/thong-tin-don-hang']);
  }
  viewProductByCategory(id: number) {
    this.router.navigate(['/san-pham'], { state: { categoryId: id } });
  }
}
