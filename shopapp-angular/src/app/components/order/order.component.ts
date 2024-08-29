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

@Component({
  selector: 'app-order',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule,RouterLink],
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
    private productService: ProductService
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
          this.productService.getProductOrders(this.productIds),
        ]).subscribe({
          next: ([sizesResponse, productsResponse]) => {
            this.size = sizesResponse;
            this.localProduct = this.cart.map((cartLocal) => {
              const product = productsResponse.find(
                (productAPI) => productAPI.id === cartLocal.product_id
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
                size:size,
                quantity: quantity,
              };
            });
            
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
  // cartItems: { product: ProductResponse; quantity: number; total: number }[] =
  //   [];
  // orderProducts: ProductResponse[] = [];
  // total_money: number = 0;
  // orderForm: FormGroup;
  // orderData: OrderDTO = {
  //   user_id: 3,
  //   fullname: '',
  //   email: '',
  //   phone_number: '',
  //   address: '',
  //   note: '',
  //   total_money: 0,
  //   payment_method: '',
  //   shipping_method: '',
  //   coupon_code: '',
  //   cart_items: [],
  // };
  // constructor(
  //   private productService: ProductService,
  //   private cartService: CartService,
  //   private fb: FormBuilder,
  //   private orderService: OrderService
  // ) {
  //   this.orderForm = this.fb.group({
  //     fullname: ['con me may', [Validators.required, Validators.maxLength(50)]],
  //     email: ['e@em', [Validators.required, Validators.email]],
  //     phone_number: [
  //       '2344234',
  //       [
  //         Validators.required,
  //         Validators.minLength(6),
  //         Validators.maxLength(20),
  //       ],
  //     ],
  //     address: ['a', [Validators.required]],
  //     note: ['', []],
  //     payment_method: ['cod', [Validators.required]],
  //     shipping_method: ['express', [Validators.required]],
  //   });
  // }
  // ngOnInit(): void {
  //   const cart = this.cartService.getCart();
  //   const productIds = Array.from(cart.keys());
  //   this.productService.getProductOrders(productIds).subscribe({
  //     next: (products) => {
  //       if (products) {
  //         this.cartItems = productIds.map((productId) => {
  //           const product = products.find((p) => p.id === productId);
  //           if (product) {
  //             product.thumbnail = `${enviroment.apiBaseUrl}/products/images/${product.thumbnail}`;
  //           }
  //           return {
  //             product: product!,
  //             quantity: cart.get(productId)!,
  //             total: product?.price! * cart.get(productId)!,
  //           };
  //         });
  //       }
  //       console.log(this.cartItems);
  //       this.setToltal();
  //     },
  //     complete: () => {},
  //     error: (error: any) => {
  //       console.log(error);
  //     },
  //   });
  // }
  // setToltal() {
  //   this.total_money = this.cartItems.reduce(
  //     (result, item) => result + item.total,
  //     0
  //   );
  // }
  // submitHandler() {
  //   if (this.orderForm.valid) {
  //     this.orderData = {
  //       ...this.orderData,
  //       ...this.orderForm.value,
  //     };
  //     this.orderData.total_money = this.total_money;
  //     this.cartItems.map((cartItem) => ({
  //       product_id: cartItem.product.id,
  //       quantity: cartItem.quantity,
  //     }));
  //     debugger;
  //     this.orderService.oder(this.orderData).subscribe({
  //       next: (response: any) => {
  //         console.log(response);
  //       },
  //       error: (error) => {
  //         console.log(error);
  //       },
  //       complete: () => {},
  //     });
  //   }
  // }
}
