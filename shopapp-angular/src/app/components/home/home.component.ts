import { Component, OnInit } from '@angular/core';
import { enviroment } from '../../enviroments/enviroment';
import { ProductService } from '../../service/product.service';
import { ProductResponse } from '../../responses/product/product.response';
import { max } from 'rxjs';
import { CommonModule } from '@angular/common';
import { CategoryService } from '../../service/category.service';
import { CategoryResponse } from '../../responses/category/category.respones';
import { BannerResponse } from '../../responses/banner.respose';
@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.component.html',
})
export class HomeComponent {
  categories: CategoryResponse[] = [];
  banners: BannerResponse[] = [];
  constructor(private categoryService: CategoryService) {}
  ngOnInit(): void {
    this.categoryService.getCategories().subscribe({
      next: (response) => {
        this.categories = response;
        this.categories.map((category) => {
          if (category.thumbnail) {
            category.thumbnail = `${enviroment.apiImage}/${category.thumbnail}`;
          }
          return category;
        });
      },
    });
    this.categoryService.getBanners().subscribe({
      next: (response) => {
        this.banners = response;
        this.banners.map((banner) => {
          banner.thumbnail = `${enviroment.apiImage}/${banner.thumbnail}`;
          return banner;
        });
      },
      error: (error) => {
        console.log(error);
      },
    });
  }
  // products: ProductResponse[] = [];
  // currentPage: number = 1;
  // limit: number = 3;
  // pages: number[] = [];
  // totalsPages: number = 0;
  // visiblePages: number[] = [];
  // categories: CategoryResponse[] = [];
  // keyword: string = '';
  // categoryId: number = 0;
  // constructor(
  //   private productService: ProductService,
  //   private categoryService: CategoryService
  // ) {}
  // ngOnInit() {
  //   this.getProducts(
  //     this.currentPage,
  //     this.limit,
  //     this.keyword,
  //     this.categoryId
  //   );
  //   this.getCategories();
  // }
  // searchProduct() {
  //   this.getProducts(
  //     this.currentPage,
  //     this.limit,
  //     this.keyword,
  //     this.categoryId
  //   );
  // }
  // getProducts(
  //   page: number,
  //   limit: number,
  //   keyword: string,
  //   categoryId: number
  // ) {
  //   this.productService
  //     .getProducts(page, limit, keyword, categoryId)
  //     .subscribe({
  //       next: (respone: any) => {
  //         respone.products.forEach((products: ProductResponse) => {
  //           products.thumbnail = `${enviroment.apiBaseUrl}/products/images/${products.thumbnail}`;
  //         });
  //         this.products = respone.products;
  //         this.totalsPages = respone.total_pages;
  //         // debugger;
  //         this.visiblePages = this.generateVisiblePageArray(
  //           this.currentPage,
  //           this.totalsPages
  //         );
  //       },
  //       complete: () => {},
  //       error: (error: any) => {
  //         console.log(error);
  //       },
  //     });
  // }
  // getCategories() {
  //   this.categoryService.getCategories().subscribe({
  //     next: (response: any) => {
  //       this.categories = response;
  //     },
  //     complete: () => {},
  //     error: (error: any) => {
  //       console.log(error);
  //     },
  //   });
  // }
  // onPageChange(page: number) {
  //   this.currentPage = page;
  //   this.getProducts(
  //     this.currentPage,
  //     this.limit,
  //     this.keyword,
  //     this.categoryId
  //   );
  // }
  // generateVisiblePageArray(currentPage: number, totalPages: number): number[] {
  //   const maxVisiblePage = 5;
  //   const halfVisiblePage = Math.floor(maxVisiblePage / 2);

  //   let startPage = Math.max(currentPage - halfVisiblePage, 1);
  //   let endPage = Math.min(startPage + maxVisiblePage - 1, totalPages);

  //   if (endPage - startPage + 1 < maxVisiblePage) {
  //     startPage = Math.max(endPage - maxVisiblePage + 1, 1);
  //   }

  //   // Đảm bảo startPage và endPage là các giá trị hợp lệ
  //   if (startPage < 1) startPage = 1;
  //   if (endPage > totalPages) endPage = totalPages;

  //   // Tính toán độ dài của mảng, đảm bảo nó là một giá trị hợp lệ
  //   const length = endPage - startPage + 1;
  //   if (length <= 0) return [];

  //   return new Array(length).fill(0).map((_, index) => startPage + index);
  // }
  // onKeywordChange(event: Event) {
  //   const element = event.target as HTMLSelectElement;
  //   this.keyword = element.value;
  //   console.log(this.keyword);
  // }
  // onCategoryChange(event: Event) {
  //   const element = event.target as HTMLSelectElement;
  //   this.categoryId = Number(element.selectedOptions[0].id);
  //   console.log(this.categoryId);
  // }
}
