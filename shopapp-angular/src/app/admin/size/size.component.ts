import { Component, OnInit } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { SizeService } from '../../service/size.service';
import { Size } from '../../responses/size.response';
import { CommonModule } from '@angular/common';
import { SweetAlertService } from '../../service/sweet-alert.service';

@Component({
  selector: 'app-size',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './size.component.html',
})
export class SizeComponent implements OnInit {
  sizes: Size[] = [];
  constructor(
    private sizeService: SizeService,
    private router: Router,
    private alert: SweetAlertService
  ) {}
  ngOnInit(): void {
    this.sizeService.getAllSize().subscribe({
      next: (response) => {
        this.sizes = response;
      },
    });
  }
  edit(id: number) {
    this.router.navigate(['/quan-ly/size/edit'], { queryParams: { id: id } });
  }
  remove(id: number) {
    this.alert.showConfirm('Cảnh báo', 'Chắc chắn xóa?').then((result) => {
      if (result.isConfirmed) {
        this.sizeService.removeSize(id).subscribe({
          next: (response) => {
            this.alert.showSuccess(response.message).then((result) => {
              if (result.isConfirmed) {
                window.location.reload();
              }
            });
          },
          error: (response) => {
            this.alert.showError(response.message);
          },
          complete() {},
        });
      }
    });
  }
  createSizeNavigate() {
    this.router.navigate(['/quan-ly/size/edit']);
  }
}
