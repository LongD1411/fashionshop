import { ProductResponse } from '../product/product.response';

export interface OrderDetailResponse {
  product: ProductResponse;
  price: number;
  number_of_products: number;
  total_money: number;
}
