import { Component, OnInit, OnDestroy } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { CartService } from '../../service/cart.service';
import { Subscription } from 'rxjs';
import { UserService } from '../../service/user.service';
import { UserResponse } from '../../responses/user/user.response';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink, CommonModule],
  templateUrl: './header.component.html',
})
export class HeaderComponent implements OnInit, OnDestroy {
  itemCartQuantity: number = 0;
  private cartSubscription!: Subscription;
  userResponse: UserResponse | undefined;
  constructor(private cartService: CartService) {}

  ngOnInit() {
    this.cartSubscription = this.cartService
      .getQuantity()
      .subscribe((quantity) => {
        this.itemCartQuantity = quantity;
      });
    
  }

  ngOnDestroy() {
    if (this.cartSubscription) {
      this.cartSubscription.unsubscribe();
    }
  }
}
