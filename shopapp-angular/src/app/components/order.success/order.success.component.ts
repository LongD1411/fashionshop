import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { OrderService } from '../../service/order.service';
import { CommonModule } from '@angular/common';
import { enviroment } from '../../enviroments/enviroment';
import { FormatDate } from '../../service/fomat.date.service';
import { CurrencyService } from '../../service/currency.service';

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
    private orderService: OrderService,
    private formatDate: FormatDate,
    public currency:CurrencyService
  ) {}

  ngOnInit(): void {
    this.orderData = history.state.orderData;
    
    if (this.orderData) {
      this.updateThumbnails(this.orderData.order_detail);
    } else {
      this.route.queryParamMap.subscribe((params) => {
        this.orderID = params.get('id');
        if (this.orderID) {
          this.orderService.getOrderSuccess(this.orderID).subscribe({
            next: (response) => {
              this.orderData = response.result;
              this.updateThumbnails(this.orderData.order_detail);
              const dob = new Date(this.orderData.created_at);
              this.orderData.created_at = this.formatDate.getFormattedDate(dob);
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
