import { Component, OnInit } from '@angular/core';
import { UserResponse } from '../../responses/user/user.response';
import { UserService } from '../../service/user.service';
import { CommonModule } from '@angular/common';
import { Event } from '@angular/router';
import { EventDispatcher } from '@angular/core/primitives/event-dispatch';
import { SweetAlertService } from '../../service/sweet-alert.service';

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user.component.html',
  styleUrl: './user.component.css',
})
export class UserComponent implements OnInit {
  user: UserResponse[] = [];
  currentPage: number = 1;
  limit: number = 10;
  pages: number[] = [];
  totalsPages: number = 0;
  visiblePages: number[] = [];
  keyword: string = '';
  constructor(
    private userService: UserService,
    private alert: SweetAlertService
  ) {}
  ngOnInit(): void {
    this.getUser(this.currentPage, this.limit, this.keyword);
  }
  getUser(currentPage: number, limit: number, keyword: string) {
    this.userService.getAllUsers(currentPage, limit, keyword).subscribe({
      next: (response) => {
        this.user = response.users;
        this.totalsPages = response.total_page;
        this.visiblePages = this.generateVisiblePageArray(
          this.currentPage,
          this.totalsPages
        );
      },
      error: (response) => {
        console.log(response);
      },
      complete() {},
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
      this.getUser(this.currentPage, this.limit, this.keyword);
    }
  }
  onKeyWordChange(event: any) {
    const element = event.target as HTMLSelectElement;
    this.keyword = element.value;
  }
  searchUser() {
    this.getUser(this.currentPage, this.limit, this.keyword);
  }
  ban(id: number) {
    this.alert
      .showConfirm('Cảnh báo', 'Chắc chắn Ban nó chưa')
      .then((result) => {
        if (result.isConfirmed) {
          this.userService.ban(id).subscribe({
            next: (response) => {
              this.alert.showSuccess(response.message).then((result) => {
                if (result.isConfirmed) window.location.reload();
              });
            },
            error: (response) => {
              this.alert.showError('Không thể cập nhật');
            },
            complete: () => {},
          });
        }
      });
  }
  unban(id: number) {
    this.alert
      .showConfirm('Cảnh báo', 'Chắc chắn Unban nó chưa')
      .then((result) => {
        if (result.isConfirmed) {
          this.userService.unban(id).subscribe({
            next: (response) => {
              this.alert.showSuccess(response.message).then((result) => {
                if (result.isConfirmed) window.location.reload();
              });
            },
            error: (response) => {
              this.alert.showError('Không thể cập nhật');
            },
            complete: () => {},
          });
        }
      });
  }
}
