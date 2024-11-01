import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { OrderService } from '../../service/order.service';
import { FormatDate } from '../../service/fomat.date.service';
import { CurrencyService } from '../../service/currency.service';
import { OrderResponse } from '../../responses/order/order.response';
import { enviroment } from '../../enviroments/enviroment';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SweetAlertService } from '../../service/sweet-alert.service';

@Component({
  selector: 'app-order.edit',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './order.edit.component.html',
})
export class OrderEditComponent implements OnInit {
  orderData?: OrderResponse;
  orderID: string | null = null;
  statusUpdate: string | null = null;
  orderStatuses = [
    { value: 'pending', label: 'Chờ xác nhận' },
    { value: 'shipped', label: 'Chờ đóng hàng' },
    { value: 'delivered', label: 'Đang vận chuyển' },
    { value: 'cancelled', label: 'Đã hủy' },
  ];
  constructor(
    private activatedRoute: ActivatedRoute,
    private orderService: OrderService,
    private formatDate: FormatDate,
    public currency: CurrencyService,
    private alert: SweetAlertService
  ) {}
  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe((param) => {
      this.orderID = param['id'];
    });
    this.orderService.getOrderSuccess(this.orderID!).subscribe({
      next: (response) => {
        this.orderData = response.result;
        this.updateThumbnails(this.orderData.order_detail);
        const dob = new Date(this.orderData.created_at);
        this.orderData.created_at = this.formatDate.getFormattedDate(dob);
      },
    });
  }
  private updateThumbnails(orderDetail: any[]): void {
    if (orderDetail) {
      orderDetail.map((product: any) => {
        product.thumbnail_url = `${enviroment.apiImage}/${product.thumbnail_url}`;
        return product;
      });
    }
  }
  onStatusChange(event: Event) {
    const selectElement = event.target as HTMLSelectElement;
    const selectedValue = selectElement.value;

    this.statusUpdate = selectedValue;
  }
  updateOrder() {
    if (this.statusUpdate == null) {
      this.statusUpdate = this.orderData!.status;
    }
    this.orderService.updateOrder(this.orderID!, this.statusUpdate).subscribe({
      next: (response) => {
        this.alert.showSuccess('Cập nhật thành công').then((result) => {
          if (result.isConfirmed) {
            window.location.reload();
          }
        });
      },
      error: (error) => {
        this.alert.showError(error.error.message);
      },
    });
  }
}
