import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { CategoryService } from '../../service/category.service';
import { BannerResponse } from '../../responses/banner.respose';
import { enviroment } from '../../enviroments/enviroment';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { SweetAlertService } from '../../service/sweet-alert.service';

@Component({
  selector: 'app-banner.edit',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './banner.edit.component.html',
  styleUrl: './banner.edit.component.css',
})
export class BannerEditComponent implements OnInit {
  selectedImage: string | ArrayBuffer | null = null;
  bannerId: number | undefined;
  banner: BannerResponse | undefined;
  bannerForm: FormGroup;
  constructor(
    private router: Router,
    private activedRoute: ActivatedRoute,
    private category: CategoryService,
    private fb: FormBuilder,
    private categoryService: CategoryService,
    private alert: SweetAlertService
  ) {
    this.bannerForm = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      file: [null, Validators.required],
    });
  }
  ngOnInit(): void {
    this.activedRoute.queryParams.subscribe((param) => {
      this.bannerId = param['id'];
    });
    if (this.bannerId) {
      this.category.getBanner(this.bannerId).subscribe({
        next: (response) => {
          this.banner = response;
          this.banner.thumbnail = `${enviroment.apiImage}/${this.banner.thumbnail}`;
          //load du lieu len man hinh
          this.bannerForm.patchValue({
            title: this.banner.title,
            description: this.banner.description,
          });
        },
      });
    }
  }
  onFileSelected(event: Event): void {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (file) {
      this.bannerForm.patchValue({ file: file });
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = (e) => {
        this.selectedImage = e.target!.result;
      };
    }
  }
  createBanner() {
    const formData = new FormData();
    const bannerDTO = {
      title: this.bannerForm.get('title')?.value,
      description: this.bannerForm.get('description')?.value,
    };
    formData.append(
      'banner',
      new Blob([JSON.stringify(bannerDTO)], { type: 'application/json' })
    );
    const file = this.bannerForm.get('file')?.value;
    if (file) {
      formData.append('file', file, file.name);
    }
    this.categoryService.createBanner(formData).subscribe({
      next: (response) => {
        this.router.navigate(['quan-ly/banner/edit']);
        this.alert.showSuccess('Tạo thành công');
      },
      error: (response) => {
        this.alert.showError('Không tạo được');
      },
    });
  }

  updateBanner() {
    this.alert.showConfirm("Cảnh báo",'Chắc chắn chưa ?').then((result) => {
      if (result.isConfirmed) {
        const formData = new FormData();
        const bannerDTO = {
          id: this.bannerId,
          title: this.bannerForm.get('title')?.value,
          description: this.bannerForm.get('description')?.value,
        };
        formData.append(
          'banner',
          new Blob([JSON.stringify(bannerDTO)], { type: 'application/json' })
        );
        const file = this.bannerForm.get('file')?.value;
        if (file) {
          formData.append('file', file, file.name);
        }
        this.categoryService.updateBanner(formData).subscribe({
          next: (response) => {
            this.router.navigate(['quan-ly/banner/edit'], {
              queryParams: { id: this.bannerId },
            });
            this.alert.showSuccess('Cập nhật thành công').then((result) => {
              if (result.isConfirmed) {
                window.location.reload();
              }
            });
          },
          error: (response) => {
            this.alert.showError('Không cập nhật được');
          },
        });
      }
    });
  }
}
