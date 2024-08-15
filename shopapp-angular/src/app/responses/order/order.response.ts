import { ProductResponse } from '../product/product.response';
import { OrderDetailResponse } from './order.detail.response';

export interface OrderResponse {
  id: number;
  full_name: string;
  email: string;
  phone_number: string;
  address: string;
  total_money: number;
  note: string;
  status: string;
  created_at: Date;
  shipping_method: string;
  payment_method: string;
  order_detail: OrderDetailResponse[];
}
