import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ProductImageResponse } from '../../responses/product/productImage.response';
import { ProductService } from '../../service/product.service';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { ProductResponse } from '../../responses/product/product.response';
import { enviroment } from '../../enviroments/enviroment';
import { CommonModule } from '@angular/common';
import { CartService } from '../../service/cart.service';
import { Size } from '../../responses/size.response';
import { SweetAlertService } from '../../service/sweet-alert.service';
import { CurrencyService } from '../../service/currency.service';
@Component({
  selector: 'app-detail-product',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './detail-product.component.html',
})
export class DetailProductComponent implements OnInit {
  productId: number | undefined;
  product: any | null = null;
  quantity: number = 1;
  top4ProductUpdated: ProductResponse[] = [];
  selectedSizeId: number | null = null;
  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private router: Router,
    private cartService: CartService,
    private alert: SweetAlertService,
    public currency:CurrencyService
  ) {}
  ngOnInit(): void {
    window.scroll(0, 0);
    this.route.queryParams.subscribe((param) => {
      this.productId = param['id'];
    });
    if (this.productId) {
      this.productService.getProduct(this.productId).subscribe({
        next: (response) => {
          this.product = response.result;
          if (this.product?.thumbnail) {
            this.product.thumbnail = `${enviroment.apiImage}/${this.product?.thumbnail}`;
          }
          if (this.product.product_images) {
            this.product.product_images.map((item: any) => {
              item.thumbnail_url = `${enviroment.apiImage}/${item.thumbnail_url}`;
              return item;
            });
          }
          // if (this.product?.sizes) {
          //   this.sortSizes(this.product.sizes);
          // }
        },
        error: (error) => {
          console.log(error);
        },
        complete: () => {},
      });
    }
    this.productService.getTop4ProductUpdated().subscribe({
      next: (response) => {
        this.top4ProductUpdated = response.results;
        this.top4ProductUpdated.map((product) => {
          product.thumbnail = `${enviroment.apiImage}/${product.thumbnail}`;
        });
      },
      error: (error) => {
        console.log(error);
      },
      complete: () => {},
    });
  }
  sortSizes(sizes: Size[]): void {
    sizes.sort((a, b) => {
      if (a.sizeName < b.sizeName) {
        return -1;
      } else if (a.sizeName > b.sizeName) {
        return 1;
      } else {
        return 0;
      }
    });
  }
  quantityUp() {
    this.quantity += 1;
  }
  quantityDown() {
    if (this.quantity >= 2) {
      this.quantity -= 1;
    }
  }
  viewProductDetails(productId: number) {
    this.router
      .navigate(['/chi-tiet-san-pham'],{queryParams: {id:productId}})
      .then(() => window.scroll(0, 0))
      .then(()=> window.location.reload());
  }
  addToCart(id: number) {
    if (id) {
      this.cartService.setCart(id, this.selectedSizeId!, this.quantity);
    }
    this.alert.showSuccess('Thêm thành công');
  }
  onSizeChange(sizeId: number): void {
    this.selectedSizeId = sizeId;
  }
}