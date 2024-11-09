import { Component, OnInit } from '@angular/core';
import { Size } from '../../responses/size.response';
import { ProductService } from '../../service/product.service';
import { CommonModule } from '@angular/common';
import { ProductResponse } from '../../responses/product/product.response';
import { enviroment } from '../../enviroments/enviroment';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CartService } from '../../service/cart.service';
import { CurrencyService } from '../../service/currency.service';
import { CategoryService } from '../../service/category.service';
import { CategoryResponse } from '../../responses/category/category.respones';
import { NgxSliderModule, Options } from '@angular-slider/ngx-slider';
import { empty } from 'rxjs';

@Component({
  selector: 'app-product',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, NgxSliderModule],
  templateUrl: './product.component.html',
  styleUrl: './product.component.css',
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
  categories: CategoryResponse[] = [];
  minValue: number = 0;
  maxValue: number = 10000000;
  constructor(
    private productService: ProductService,
    private router: Router,
    private cartService: CartService,
    public currency: CurrencyService,
    private categoryService: CategoryService
  ) {}
  ngOnInit(): void {
    if (history.state.categoryId) {
      this.categoryId = history.state.categoryId;
    }
    this.getProducts(
      this.currentPage,
      this.limit,
      this.keyword,
      this.categoryId,
      this.minValue,
      this.maxValue
    );
    this.categoryService.getCategories().subscribe({
      next: (response) => {
        this.categories = response.results;
      },
      error: (error) => {},
    });
  }

  changePage(page: number): void {
    if (page >= 1 && page <= this.totalsPages) {
      this.currentPage = page;
      this.getProducts(
        this.currentPage,
        this.limit,
        this.keyword,
        this.categoryId,
        this.minValue,
        this.maxValue
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
    categoryId: number,
    minValue: number,
    maxValue: number
  ) {
    if (minValue == 0 && maxValue == 10000000) {
      minValue = 0;
      maxValue = 1000000000;
    }
    this.productService
      .getProducts(page, limit, keyword, categoryId, minValue, maxValue)
      .subscribe({
        next: (respone: any) => {
          respone.results.forEach((products: ProductResponse) => {
            products.thumbnail = `${enviroment.apiImage}/${products.thumbnail}`;
          });
          this.products = respone.results;
          this.totalsPages = respone.totalPage;
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
  }

  viewProductDetails(productId: number) {
    this.router.navigate(['/chi-tiet-san-pham'], {
      queryParams: { id: productId },
    });
  }
  searchProduct() {
    this.getProducts(
      this.currentPage,
      this.limit,
      this.keyword,
      this.categoryId,
      this.minValue,
      this.maxValue
    );
  }
  onStatusChange(event: Event) {
    const selectElement = event.target as HTMLSelectElement;
    const selectedValue = selectElement.value;

    this.categoryId = Number(selectedValue);
    this.getProducts(
      this.currentPage,
      this.limit,
      this.keyword,
      this.categoryId,
      this.minValue,
      this.maxValue
    );
  }
  sliderOptions: Options = {
    floor: this.minValue,
    ceil: this.maxValue,
    step: 1,
    showTicksValues: false,
    showSelectionBar: true,
    hideLimitLabels: true,
    hidePointerLabels: true,
  };
}
