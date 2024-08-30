import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CategoryResponse } from '../../responses/category/category.respones';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { CategoryService } from '../../service/category.service';
import { enviroment } from '../../enviroments/enviroment';
import { SweetAlertService } from '../../service/sweet-alert.service';

@Component({
  selector: 'app-category.edit',
  standalone: true,
  imports: [RouterLink, CommonModule, ReactiveFormsModule],
  templateUrl: './category.edit.component.html',
  styleUrl: './category.edit.component.css',
})
export class CategoryEditComponent implements OnInit {
  category: CategoryResponse | undefined;
  categoryId: number | undefined;
  selectedImage: string | ArrayBuffer | null = null;
  categoryForm: FormGroup;
  constructor(
    private activeRoute: ActivatedRoute,
    private fb: FormBuilder,
    private categoryService: CategoryService,
    private alert: SweetAlertService,
    private router: Router
  ) {
    this.categoryForm = this.fb.group({
      name: ['', Validators.required],
      file: [null],
    });
  }
  ngOnInit(): void {
    this.activeRoute.queryParams.subscribe((param) => {
      this.categoryId = param['id'];
    });
    if (this.categoryId) {
      this.categoryService.getCategory(this.categoryId).subscribe({
        next: (response) => {
          this.category = response;
          if (response.thumbnail) {
            this.category.thumbnail = `${enviroment.apiImage}/${this.category.thumbnail}`;
          }
          this.categoryForm.patchValue({
            name: this.category.name,
          });
        },
        error: (error) => {
          console.log(error);
        },
      });
    }
  }
  onFileSelected(event: Event): void {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (file) {
      this.categoryForm.patchValue({ file: file });
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = (e) => {
        this.selectedImage = e.target!.result;
      };
    }
  }
  createCategory() {
    const name = this.categoryForm.get('name')?.value;
    if (name) {
      const formData = new FormData();
      const categoryDTO = { name: name };
      formData.append(
        'name',
        new Blob([JSON.stringify(categoryDTO)], { type: 'application/json' })
      );
      const file = this.categoryForm.get('file')?.value;
      if (file) {
        formData.append('file', file, file.name);
      }
      this.categoryService.createCategory(formData).subscribe({
        next: (response) => {
          this.router.navigate(['quan-ly/category/edit'], {
            queryParams: { id: response.id },
          });
          this.alert.showSuccess('Tạo thành công').then((result) => {
            if (result.isConfirmed) {
              window.location.reload();
            }
          });
        },
      });
    }
  }

  updateCategory() {
    this.alert.showConfirm('Cảnh báo', 'Chắc chắn chưa ?').then((result) => {
      if (result.isConfirmed) {
        const formData = new FormData();
        const categoryDTO = {
          id: this.category?.id,
          name: this.categoryForm.get('name')?.value,
        };
        formData.append(
          'category',
          new Blob([JSON.stringify(categoryDTO)], { type: 'application/json' })
        );
        const file = this.categoryForm.get('file')?.value;
        if (file) {
          formData.append('file', file, file.name);
        }
        this.categoryService.updateCategory(formData).subscribe({
          next: (response) => {
            this.router.navigate(['quan-ly/category/edit'], {
              queryParams: { id: response.id },
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
