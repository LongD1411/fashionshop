import { Component, OnInit } from '@angular/core';
import { OrderService } from '../../service/order.service';
import { OrderResponse } from '../../responses/order/order.response';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { CurrencyService } from '../../service/currency.service';

@Component({
  selector: 'app-user.order',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './user.order.component.html',
  styleUrl: './user.order.component.css',
})
export class UserOrderComponent implements OnInit {
  oders: OrderResponse[] = [];
  userId: number | undefined;
  constructor(
    private orderService: OrderService,
    private activeRoute: ActivatedRoute,
    private router: Router,
    public currency: CurrencyService
  ) {}
  ngOnInit(): void {
    this.orderService.getAllOrder().subscribe({
      next: (response) => {
        this.oders = response.results;
      },
      error: (error) => {},
      complete() {},
    });
  }
  orderDetail(orderId: number) {
    this.router.navigate(['/thong-tin-don-hang'], {
      queryParams: { id: orderId },
    });
  }
}
