import { ProductResponse } from '../product/product.response';

export interface OrderDetailResponse {
  product: ProductResponse;
  quantity: number;
  total_money: number;
}
