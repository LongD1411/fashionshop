import { Component, OnInit } from '@angular/core';

import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../service/product.service';
import { ProductImageResponse } from '../../responses/product/productImage.response';
import { enviroment } from '../../enviroments/enviroment';
import { OrderDTO } from '../../dtos/order/order.dto';
import { OrderService } from '../../service/order.service';
import { OrderResponse } from '../../responses/order/order.response';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-oder-confirm',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './oder-detail.component.html',
})
export class OderConfirmComponent implements OnInit {
  orderResponse: OrderResponse | undefined;
  constructor(
    private route: ActivatedRoute,
    private orderService: OrderService
  ) {}
  ngOnInit(): void {
    const orderId = this.route.snapshot.paramMap.get('id');
    if (orderId != null) {
      this.orderService.getOrderDetail(orderId).subscribe({
        next: (response) => {
          this.orderResponse = response;
          if (this.orderResponse?.order_detail !== undefined) {
            this.orderResponse.order_detail =
              this.orderResponse?.order_detail.map((orderDetail) => {
                orderDetail.product.thumbnail = `${enviroment.apiBaseUrl}/products/images/${orderDetail.product.thumbnail}`;
                return orderDetail;
              });
          }
        },
        complete() {},
        error(err) {
          console.log(err);
        },
      });
    }
  }
}
