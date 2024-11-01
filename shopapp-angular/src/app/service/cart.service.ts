import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { ProductResponse } from '../responses/product/product.response';
import { CartItemStorage } from '../responses/cart.item';
import { CurrencyService } from './currency.service';

@Injectable({
  providedIn: 'root',
})
export class CartService {
  private cart: CartItemStorage[] = [];
  private cartQuantity: BehaviorSubject<number>;
  private realCart: {
    product: ProductResponse | undefined;
    size: string | undefined;
    quantity: number;
  }[] = [];
  private cartSubject: BehaviorSubject<CartItemStorage[]>;
  constructor(public currency: CurrencyService) {
    const storeCart = localStorage.getItem('Cart');
    if (storeCart) {
      this.cart = JSON.parse(storeCart);
    }
[]
    this.cartQuantity = new BehaviorSubject<number>(this.cart.length);
    this.cartSubject = new BehaviorSubject<CartItemStorage[]>(this.cart);
  }

  setCart(product_id: number, size_id: number, quantity: number) {
    const item = this.cart.find(
      (item) => item.product_id == product_id && item.size_id == size_id
    );
    if (item) {
      item.quantity += quantity;
      this.setCartToLocal();
      this.cartQuantity.next(this.cart.length);
    } else {
      this.cart.push({
        product_id: product_id,
        size_id: size_id,
        quantity: quantity,
      });
      this.setCartToLocal();
      this.cartQuantity.next(this.cart.length);
    }
  }

  private setCartToLocal() {
    localStorage.setItem('Cart', JSON.stringify(this.cart));
  }

  getCart(): Observable<CartItemStorage[]> {
    return this.cartSubject.asObservable();
  }
  quantityUp(id: number) {
    this.cart.forEach((item) => {
      if (item.product_id == id) {
        item.quantity += 1;
      }
    });
    this.updateCart(this.cart);
  }

  quantityDown(id: number) {
    this.cart.forEach((item) => {
      if (item.product_id == id && item.quantity >= 2) {
        item.quantity -= 1;
      }
    });
    this.updateCart(this.cart);
  }
  getQuantity(): Observable<number> {
    return this.cartQuantity.asObservable();
  }
  removeItem(id: number): void {
    this.cart = this.cart.filter((item) => item.product_id != id);
    this.updateCart(this.cart);
  }
  removeCart() {
    localStorage.removeItem('Cart');
    this.updateCart([]);
  }
  updateCart(cart: CartItemStorage[]): void {
    this.cart = cart;
    this.setCartToLocal();
    this.cartSubject.next(this.cart);
    this.cartQuantity.next(this.cart.length);
  }
}
