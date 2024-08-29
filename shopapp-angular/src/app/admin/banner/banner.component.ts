import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { CategoryService } from '../../service/category.service';
import { BannerResponse } from '../../responses/banner.respose';
import { enviroment } from '../../enviroments/enviroment';
import { ActivatedRoute, Router } from '@angular/router';
import { SweetAlertService } from '../../service/sweet-alert.service';

@Component({
  selector: 'app-image-upload',
  standalone: true,
  templateUrl: './banner.component.html',
  styleUrls: ['./banner.component.css'],
  imports: [CommonModule],
})
export class BannerComponent implements OnInit {
  banners: BannerResponse[] = [];
  bannerId: number | undefined;
  constructor(
    private categoryService: CategoryService,
    private router: Router,
    private activedRoute: ActivatedRoute,
    private alert: SweetAlertService
  ) {}
  ngOnInit(): void {
    this.activedRoute.queryParams.subscribe((param) => {
      this.bannerId = param['id'];
    });
    this.categoryService.getAllBanners().subscribe({
      next: (response) => {
        this.banners = response;
        this.banners.map((item) => {
          item.thumbnail = `${enviroment.apiImage}/${item.thumbnail}`;
          return item;
        });
      },
    });
  }
  createBanner() {
    this.router.navigate(['quan-ly/banner/edit']);
  }
  updatedBanner(id: number) {
    this.router.navigate(['quan-ly/banner/edit'], { queryParams: { id } });
  }
  deleteBanner(id: number) {
    this.alert.showConfirm('Cảnh báo', 'Chắc chắn xóa?').then((result) => {
      if (result.isConfirmed) {
        this.categoryService.deleteBanner(id).subscribe({
          next: (response) => {
            this.alert.showSuccess(response.message).then((result) => {
              if (result.isConfirmed) {
                window.location.reload();
              }
            });
          },
          error: (response) => {
            this.alert.showError(response.message);
          },
          complete() {},
        });
      }
    });
  }
}
