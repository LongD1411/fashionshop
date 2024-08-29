import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { OrderService } from '../../service/order.service';
import { CommonModule } from '@angular/common';
import { enviroment } from '../../enviroments/enviroment';

@Component({
  selector: 'app-order.success',
  standalone: true,
  imports: [RouterLink, CommonModule],
  templateUrl: './order.success.component.html',
  styleUrls: ['./order.success.component.css'],
})
export class OrderSuccessComponent implements OnInit {
  orderData: any;
  orderID: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private orderService: OrderService
  ) {}

  ngOnInit(): void {
    this.orderData = history.state.orderData;
    
    if (this.orderData) {
      this.updateThumbnails(this.orderData.order_detail);
    } else {
      this.route.queryParamMap.subscribe((params) => {
        this.orderID = params.get('mdh');
        if (this.orderID) {
          this.orderService.getOrderSuccess(this.orderID).subscribe({
            next: (response) => {
              this.orderData = response;
              this.updateThumbnails(this.orderData.order_detail);
            },
            error: (error) => {
              console.log(error);
            },
            complete: () => {},
          });
        }
      });
    }
  }

  private updateThumbnails(orderDetail: any[]): void {
    if (orderDetail) {
      orderDetail.map((product: any) => {
        product.thumbnail_url = `${enviroment.apiImage}/${product.thumbnail_url}`;
        return product;
      });
    }
  }
}
