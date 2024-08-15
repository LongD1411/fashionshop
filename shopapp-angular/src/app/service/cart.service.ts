import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class CartService {
  cart: Map<number, number>;
  constructor() {
    this.cart = new Map();
    const storeCart = localStorage.getItem('Cart');
    if (storeCart) {
      this.cart = new Map(JSON.parse(storeCart));
    }
  }
  setCart(id: number, quantity: number) {
    if (this.cart.has(id)) {
      this.cart?.set(id, this.cart.get(id)! + quantity);
    } else {
      this.cart?.set(id, quantity);
    }
    this.setCartToLocal();
  }
  setCartToLocal() {
    if (this.cart)
      localStorage.setItem(
        'Cart',
        JSON.stringify(Array.from(this.cart?.entries()))
      );
  }
  getCart() {
    return this.cart;
  }
}
