import {
  AfterContentInit,
  Component,
  ElementRef,
  OnInit,
  ViewChild,
} from '@angular/core';

import {
  ActivatedRoute,
  NavigationEnd,
  Route,
  Router,
  RouterLink,
} from '@angular/router';
import { ProductService } from '../../service/product.service';
import { ProductImageResponse } from '../../responses/product/productImage.response';
import { enviroment } from '../../enviroments/enviroment';
import { OrderDTO } from '../../dtos/order/order.dto';
import { OrderService } from '../../service/order.service';
import { OrderResponse } from '../../responses/order/order.response';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { OrderComponent } from '../order/order.component';
import { ProductResponse } from '../../responses/product/product.response';
import { forkJoin, Subscription } from 'rxjs';
import { CartService } from '../../service/cart.service';
import { CartItemStorage } from '../../responses/cart.item';
import { Size } from '../../responses/size.response';
import { SweetAlertService } from '../../service/sweet-alert.service';
import { CurrencyService } from '../../service/currency.service';

@Component({
  selector: 'app-oder-confirm',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './oder-detail.component.html',
})
export class OderConfirmComponent implements OnInit, AfterContentInit {
  @ViewChild('scrollTarget', { static: true }) scrollTarget!: ElementRef;
  localProduct: any[] = [];
  orderForm: FormGroup;
  orderData: OrderDTO = {
    user_id: 3,
    full_name: '',
    email: '',
    phone_number: '',
    address: '',
    note: '',
    total_money: 0,
    payment_method: '',
    shipping_method: '',
    coupon_code: '',
    cart_items: [],
  };
  cartSubcription: Subscription | undefined;
  cart: CartItemStorage[] = [];
  productIds: number[] = [];
  size: Size[] = [];
  constructor(
    private orderService: OrderService,
    private fb: FormBuilder,
    private cartService: CartService,
    private productService: ProductService,
    private router: Router,
    private alert: SweetAlertService,
    public currency: CurrencyService
  ) {
    this.orderForm = this.fb.group({
      full_name: ['', [Validators.required, Validators.maxLength(50)]],
      email: ['', [Validators.required, Validators.email]],
      phone_number: [
        '',
        [
          Validators.required,
          Validators.minLength(5),
          Validators.maxLength(15),
        ],
      ],
      shipping_address: ['', [Validators.required]],
      note: ['', []],
      payment_method: ['cod', [Validators.required]],
      shipping_method: ['Viettel Post', [Validators.required]],
    });
  }
  ngOnInit(): void {
    this.cartSubcription = this.cartService.getCart().subscribe((items) => {
      this.cart = items;
      this.productIds = this.cart.map((item) => item.product_id);
      // Lấy thông tin kích thước và sản phẩm đồng thời
      if (this.cart.length == 0) {
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
                (sizeAPI) => sizeAPI.id == cartLocal.size_id
              );
              const quantity = cartLocal.quantity;
              return {
                product: product,
                size: size,
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
  ngAfterContentInit(): void {
    this.scrollToTop();
  }
  scrollToTop() {
    this.scrollTarget.nativeElement.scrollIntoView({
      behavior: 'smooth',
      block: 'center',
    });
  }
  getTotal(): number {
    return this.localProduct.reduce(
      (sum, item) => sum + item.product!.price * item.quantity,
      0
    );
  }
  submitHandler() {
    this.orderData = {
      ...this.orderData,
      ...this.orderForm.value,
    };
    this.orderData.total_money = this.getTotal();
    this.orderData.cart_items = this.cart;
    this.orderService.oder(this.orderData).subscribe({
      next: (response) => {
        this.cartService.removeCart();
        this.router.navigate(['/thong-tin-don-hang'], {
          state: { orderData: response },
          queryParams: { id: response.result.id },
        });
        this.alert.showSuccess('Đặt hàng thành công');
      },
      error: (error) => {
        console.log(error);
        if ((error.error.status = 400))
          this.alert.showError('Bạn cần đăng nhập để đặt hàng');
      },
      complete: () => { },
    });
  }
}
