import { Component, Injectable, OnInit } from '@angular/core';
import { ProductService } from '../../service/product.service';
import { ProductResponse } from '../../responses/product/product.response';
import { enviroment } from '../../enviroments/enviroment';
import { CommonModule } from '@angular/common';
import { CartService } from '../../service/cart.service';
import { OrderDTO } from '../../dtos/order/order.dto';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { OrderService } from '../../service/order.service';
import { Size } from '../../responses/size.response';
import { errorContext } from 'rxjs/internal/util/errorContext';
import { OrderDetailResponse } from '../../responses/order/order.detail.response';
import { Subscription, forkJoin } from 'rxjs';
import { CartItemStorage } from '../../responses/cart.item';
import { Router, RouterLink } from '@angular/router';
import { CurrencyService } from '../../service/currency.service';

@Component({
  selector: 'app-order',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './order.component.html',
})
export class OrderComponent implements OnInit {
  productIds: number[] = [];
  size: Size[] = [];
  localProduct: {
    product: ProductResponse | undefined;
    size: Size | undefined;
    quantity: number;
  }[] = [];
  cart: CartItemStorage[] = [];
  cartSubcription: Subscription | undefined;
  constructor(
    private cartService: CartService,
    private route: Router,
    private productService: ProductService,
    public currency :CurrencyService
  ) {}
  ngOnInit(): void {
    this.cartSubcription = this.cartService.getCart().subscribe((items) => {
      this.cart = items;
      this.productIds = this.cart.map((item) => item.product_id);
      // Lấy thông tin kích thước và sản phẩm đồng thời
      if (this.cart.length === 0) {
        this.localProduct = [];
      } else {
        forkJoin([
          this.productService.getAllSize(),
          this.productService.getProductByCart(this.productIds),
        ]).subscribe({
          next: ([sizesResponse, productsResponse]) => {
            this.size = sizesResponse.results;
            this.localProduct = this.cart.map((cartLocal) => {
              const product = productsResponse.results.find(
                (productAPI) => productAPI.id == cartLocal.product_id
              );
              if (product) {
                product.thumbnail = `${enviroment.apiImage}/${product.thumbnail}`;
              }
              const size = this.size.find(
                (sizeAPI) => sizeAPI.id === cartLocal.size_id
              );
              const quantity = cartLocal.quantity;
              return {
                product: product,
                size: size,
                quantity: quantity,
              };
            });
            console.log(this.localProduct, sizesResponse);
          },
          error: (error) => {
            console.log(error);
          },
        });
      }
    });
  }
  quantityUp(id: number): void {
    this.cartService.quantityUp(id);
  }
  removeItem(id: number) {
    this.cartService.removeItem(id);
  }
  quantityDown(id: number): void {
    this.cartService.quantityDown(id);
  }
  ngOnDestroy(): void {
    if (this.cartSubcription) {
      this.cartSubcription.unsubscribe();
    }
  }
  getTotal(): number {
    return this.localProduct.reduce(
      (sum, item) => sum + item.product!.price * item.quantity,
      0
    );
  }
  toConfirm() {
    this.route.navigate(['/xac-nhan']);
  }
}
