import { ProductImageResponse } from './productImage.response';

export interface ProductResponse {
  id: number;
  name: string;
  price: number;
  old_price: number;
  thumbnail: string;
  description: string;
  created_at: Date;
  updated_at: Date;
  category_id: number;
  product_images: ProductImageResponse[];
}
