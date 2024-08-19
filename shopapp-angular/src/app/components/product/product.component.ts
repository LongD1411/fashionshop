import { Component, OnInit } from '@angular/core';
import { Size } from '../../responses/size.response';
import { ProductService } from '../../service/product.service';
import { CommonModule } from '@angular/common';
import { ProductResponse } from '../../responses/product/product.response';
import { enviroment } from '../../enviroments/enviroment';
import { ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CartService } from '../../service/cart.service';

@Component({
  selector: 'app-product',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './product.component.html',
})
export class ProductComponent implements OnInit {
  sizes: Size[] = [];
  currentPage: number = 1;
  limit: number = 5;
  pages: number[] = [];
  totalsPages: number = 0;
  visiblePages: number[] = [];
  keyword: string = '';
  categoryId: number = 0;
  products: ProductResponse[] = [];
  constructor(private productService: ProductService, private router: Router, private cartService: CartService) {}
  ngOnInit(): void {
    this.getProducts(
      this.currentPage,
      this.limit,
      this.keyword,
      this.categoryId
    );
    this.productService.getAllSize().subscribe({
      next: (response) => {
        this.sizes = response;
        console.log(this.sizes);
      },
      error: (error) => {
        console.log(error);
      },
      complete: () => {},
    });
  }

  changePage(page: number): void {
    if (page >= 1 && page <= this.totalsPages) {
      this.currentPage = page;
      this.getProducts(
        this.currentPage,
        this.limit,
        this.keyword,
        this.categoryId
      );
    }
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
  onKeywordChange(event: Event) {
    const element = event.target as HTMLSelectElement;
    this.keyword = element.value;
    console.log(this.keyword);
  }
  viewProductDetails(productId: number) {
    this.router.navigate(['/chi-tiet-san-pham', productId]);
  }
  searchProduct() {
    this.getProducts(
      this.currentPage,
      this.limit,
      this.keyword,
      this.categoryId
    );
  }
}
