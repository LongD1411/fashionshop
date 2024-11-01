import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { OrderService } from '../../service/order.service';
import { OrderResponse } from '../../responses/order/order.response';
import { Router } from '@angular/router';
import { SweetAlertService } from '../../service/sweet-alert.service';

@Component({
  selector: 'app-order',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './order.component.html',
})
export class OrderComponentAdmin implements OnInit {
  orders: OrderResponse[] = [];
  currentPage: number = 1;
  limit: number = 5;
  pages: number[] = [];
  totalsPages: number = 0;
  visiblePages: number[] = [];
  keyword: string = '';
  orderStatuses = [
    { value: 'pending', label: 'Chờ xác nhận' },
    { value: 'shipped', label: 'Chờ đóng hàng' },
    { value: 'delivered', label: 'Đang vận chuyển' },
    { value: 'cancelled', label: 'Đã hủy' },
  ];
  constructor(private orderService: OrderService, private router: Router, private alert:SweetAlertService) {}
  ngOnInit(): void {
    this.getOrders(this.currentPage, this.limit, this.keyword);
  }
  getOrders(page: number, limit: number, keyword: string) {
    this.orderService.getAllOrderAdmin(page, limit, keyword).subscribe({
      next: (respone) => {
        this.orders = respone.results;
        this.totalsPages = respone.totalPage;
        this.visiblePages = this.generateVisiblePageArray(
          this.currentPage,
          this.totalsPages
        );
      },
      complete: () => {},
      error: (error: any) => {
        console.log(error);
      },
    });
  }
  generateVisiblePageArray(currentPage: number, totalPages: number): number[] {
    const maxVisiblePage = 5;
    const halfVisiblePage = Math.floor(maxVisiblePage / 2);

    let startPage = Math.max(currentPage - halfVisiblePage, 1);
    let endPage = Math.min(startPage + maxVisiblePage - 1, totalPages);

    if (endPage - startPage + 1 < maxVisiblePage) {
      startPage = Math.max(endPage - maxVisiblePage + 1, 1);
    }

    // Đảm bảo startPage và endPage là các giá trị hợp lệ
    if (startPage < 1) startPage = 1;
    if (endPage > totalPages) endPage = totalPages;

    // Tính toán độ dài của mảng, đảm bảo nó là một giá trị hợp lệ
    const length = endPage - startPage + 1;
    if (length <= 0) return [];

    return new Array(length).fill(0).map((_, index) => startPage + index);
  }
  changePage(page: number): void {
    if (page >= 1 && page <= this.totalsPages) {
      this.currentPage = page;
      this.getOrders(this.currentPage, this.limit, this.keyword);
    }
  }
  updateOrder(id: number) {
    this.router.navigate(['/quan-ly/don-hang/edit'], { queryParams: { id } });
  }
  getOrderStatusValue(statusLabel: string) {
    return this.orderStatuses.find(status => status.value == statusLabel)?.label || 'Trạng thái không xác định';
  }
  removeOrder(id: number){
    this.alert.showConfirm("Cảnh báo","Xác nhận hủy đơn").then((result)=>{
      if(result.isConfirmed){
        this.orderService.updateOrder(id.toString(), "cancelled").subscribe({
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
    })
  }
}
