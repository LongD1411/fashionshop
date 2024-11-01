import { Component, OnInit } from '@angular/core';
import { Size } from '../../responses/size.response';
import { SizeService } from '../../service/size.service';
import { ActivatedRoute, Router } from '@angular/router';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { SweetAlertService } from '../../service/sweet-alert.service';

@Component({
  selector: 'app-size',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './size.edit.component.html',
})
export class SizeEditComponent implements OnInit {
  size: Size | undefined;
  sizeId: number | undefined;
  sizeForm: FormGroup;
  sizeDTO: { id: number; size_name: string; size_code: string } | undefined;
  constructor(
    private sizeService: SizeService,
    private activedRoute: ActivatedRoute,
    private fb: FormBuilder,
    private alert: SweetAlertService
  ) {
    this.sizeForm = this.fb.group({
      size_name: ['', Validators.required],
      size_code: ['', Validators.required],
    });
  }
  ngOnInit(): void {
    this.activedRoute.queryParams.subscribe((param) => {
      this.sizeId = param['id'];
    });
    if (this.sizeId) {
      this.sizeService.getSize(this.sizeId).subscribe({
        next: (response) => {
          this.size = response.result;
          this.sizeForm.patchValue({
            size_name: this.size?.sizeName,
            size_code: this.size?.sizeCode,
          });
        },
      });
    }
  }
  createSize() {
    this.sizeService.createSize(this.sizeForm.value).subscribe({
      next: (response) => {
        this.sizeForm.patchValue({
          size_name: response.sizeName,
          size_code: response.sizeCode,
        });
        this.alert.showSuccess('Thêm thành công');
      },
      error: (error) => {
        console.log(error);
        this.alert.showError('Không thể thêm size');
      },
    });
  }
  updateSize() {
    this.sizeDTO = {
      ...this.sizeForm.value,
    };
    if (this.sizeId) {
      this.sizeDTO!.id = this.sizeId;
    }
    this.sizeService.updateSize(this.sizeDTO).subscribe({
      next: (response) => {
        this.sizeForm.patchValue({
          size_name: response.sizeName,
          size_code: response.sizeCode,
        });
        this.alert.showSuccess('Cập nhật thành công');
      },
      error: (error) => {
        console.log(error);
        this.alert.showError('Không thể thêm size');
      },
      complete() {},
    });
  }
}
