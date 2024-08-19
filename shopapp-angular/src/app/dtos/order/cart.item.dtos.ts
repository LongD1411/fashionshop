export class CartItem {
  product_id: number;
  size_id: number;
  quantity: number;
  constructor(data: any) {
    this.product_id = data.product_id;
    this.size_id = data.size_id;
    this.quantity = data.quantity;
  }
}
