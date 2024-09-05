import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ProductResponse } from '../../responses/product/product.response';
import { ProductService } from '../../service/product.service';
import { Router } from '@angular/router';
import { enviroment } from '../../enviroments/enviroment';
import { SweetAlertService } from '../../service/sweet-alert.service';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-product',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './product.component.html',
  styleUrl: './product.component.css',
})
export class AdminProductComponent implements OnInit {
  products: ProductResponse[] = [];
  currentPage: number = 1;
  limit: number = 5;
  pages: number[] = [];
  totalsPages: number = 0;
  visiblePages: number[] = [];
  keyword: string = '';
  categoryId: number = 0;
  constructor(
    private productService: ProductService,
    private router: Router,
    private alert: SweetAlertService
  ) {}
  ngOnInit(): void {
    this.getProducts(
      this.currentPage,
      this.limit,
      this.keyword,
      this.categoryId
    );
  }

  getProducts(
    page: number,
    limit: number,
    keyword: string,
    categoryId: number
  ) {
    this.productService
      .getProducts(page, limit, keyword, categoryId)
      .subscribe({
        next: (respone: any) => {
          respone.products.forEach((products: ProductResponse) => {
            products.thumbnail = `${enviroment.apiImage}/${products.thumbnail}`;
          });
          this.products = respone.products;
          this.totalsPages = respone.total_pages;
          this.visiblePages = this.generateVisiblePageArray(
            this.currentPage,
            this.totalsPages
          );
        },
        complete: () => {},
        error: (error: any) => {
          console.log(error);
        },
      });
  }
  generateVisiblePageArray(currentPage: number, totalPages: number): number[] {
    const maxVisiblePage = 5;
    const halfVisiblePage = Math.floor(maxVisiblePage / 2);

    let startPage = Math.max(currentPage - halfVisiblePage, 1);
    let endPage = Math.min(startPage + maxVisiblePage - 1, totalPages);

    if (endPage - startPage + 1 < maxVisiblePage) {
      startPage = Math.max(endPage - maxVisiblePage + 1, 1);
    }

    // Đảm bảo startPage và endPage là các giá trị hợp lệ
    if (startPage < 1) startPage = 1;
    if (endPage > totalPages) endPage = totalPages;

    // Tính toán độ dài của mảng, đảm bảo nó là một giá trị hợp lệ
    const length = endPage - startPage + 1;
    if (length <= 0) return [];

    return new Array(length).fill(0).map((_, index) => startPage + index);
  }
  changePage(page: number): void {
    if (page >= 1 && page <= this.totalsPages) {
      this.currentPage = page;
      this.getProducts(this.currentPage, this.limit, this.keyword, 0);
    }
  }
  createProduct() {
    this.router.navigate(['quan-ly/product/edit']);
  }
  updateProduct(id: number) {
    this.router.navigate(['quan-ly/product/edit'], { queryParams: { id } });
  }
  deleteProduct(id: number) {
    this.alert.showConfirm('Cảnh báo', 'Chắc chắn xóa?').then((result) => {
      if (result.isConfirmed) {
        this.productService.deleteProduct(id).subscribe({
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
