import { Component, OnInit } from '@angular/core';
import { CategoryResponse } from '../../responses/category/category.respones';
import { CategoryService } from '../../service/category.service';
import { enviroment } from '../../enviroments/enviroment';
import { SweetAlertService } from '../../service/sweet-alert.service';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-category',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './category.component.html',
  styleUrl: './category.component.css',
})
export class CategoryComponent implements OnInit {
  categories: CategoryResponse[] = [];
  constructor(
    private categoryService: CategoryService,
    private alert: SweetAlertService,
    private router: Router
  ) {}
  ngOnInit(): void {
    this.categoryService.getCategories().subscribe({
      next: (response) => {
        this.categories = response.results;
        this.categories.map((item) => {
          if (item.thumbnail) {
            item.thumbnail = `${enviroment.apiImage}/${item.thumbnail}`;
          }
          return item;
        });
      },
      error: (respone) => {
        this.alert.showError('Không thể lấy dữ liệu');
      },
    });
  }
  createNavigate() {
    this.router.navigate(['quan-ly/category/edit']);
  }
  updateNavigate(id: number) {
    this.router.navigate(['quan-ly/category/edit'], {
      queryParams: { id: id },
    });
  }
  deleteCategory(id: number) {
    this.alert.showConfirm('Cảnh báo', 'Chắc chắn xóa?').then((result) => {
      if (result.isConfirmed) {
        this.categoryService.deleteCategory(id).subscribe({
          next: (response) => {
            this.alert.showSuccess("Xóa thành công").then((result) => {
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
