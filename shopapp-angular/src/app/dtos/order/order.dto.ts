import { CartItem } from './cart.item.dtos';

export class OrderDTO {
  user_id: number;
  full_name: string;
  email: string;
  phone_number: string;
  address: string;
  note: string;
  total_money: number;
  payment_method: string;
  shipping_method: string;
  coupon_code: string;
  cart_items: CartItem[];
  constructor(data: any) {
    this.full_name = data.fullname;
    this.user_id = data.user_id;
    this.email = data.email;
    this.address = data.address;
    this.payment_method = data.payment_method;
    this.phone_number = data.phone_number;
    this.note = data.note;
    this.total_money = data.total_money;
    this.shipping_method = data.shippng_method;
    this.coupon_code = data.coupon_code;
    this.cart_items = data.cart_items;
  }
}
