import { ProductResponse } from '../product/product.response';

export interface OrderDetailResponse {
  product_name: string;
  quantity: number;
  total_money: number;
  id: number;
  price: number;
  thumbnail_url: string;
  size_name: string;
}
