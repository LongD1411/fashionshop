import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../service/product.service';
import { SweetAlertService } from '../../service/sweet-alert.service';
import {
  FormArray,
  FormBuilder,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ProductResponse } from '../../responses/product/product.response';
import { CategoryService } from '../../service/category.service';
import { CategoryResponse } from '../../responses/category/category.respones';
import { forkJoin } from 'rxjs';
import { SizeService } from '../../service/size.service';
import { Size } from '../../responses/size.response';

@Component({
  selector: 'app-product.edit',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './product.edit.component.html',
  styleUrl: './product.edit.component.css',
})
export class ProductEditComponent implements OnInit {
  productForm: FormGroup;
  selectedImage: string | ArrayBuffer | null = null;
  product: ProductResponse | undefined;
  productId: number | undefined;
  categories: CategoryResponse[] = [];
  sizes: Size[] = [];
  constructor(
    private activatedRoute: ActivatedRoute,
    private productService: ProductService,
    private alert: SweetAlertService,
    private fb: FormBuilder,
    private categoryService: CategoryService,
    private sizeService: SizeService
  ) {
    this.productForm = this.fb.group({
      name: ['', Validators.required],
      price: ['', Validators.required],
      old_price: ['', Validators.required],
      description: ['', Validators.required],
      category_id: ['', Validators.required],
      sku: ['', Validators.required],
      product_sizes: this.fb.array([]),
      thumbnail: [null, Validators.required],
      detail_images: this.fb.array([]),
    });
  }
  get productSizes(): FormArray {
    return this.productForm.get('product_sizes') as FormArray;
  }

  get detailImages(): FormArray {
    return this.productForm.get('detail_images') as FormArray;
  }

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe((param) => {
      this.productId = param['id'];
    });

    forkJoin([
      this.categoryService.getCategories(),
      this.sizeService.getAllSize(),
    ]).subscribe({
      next: ([categoriesResponse, sizesResponse]) => {
        this.categories = categoriesResponse;
        this.sizes = sizesResponse;
      },
    });
  }
  addProductSize() {
    const sizeGroup = this.fb.group({
      size_id: ['', Validators.required],
      quantity: [0, Validators.required],
    });
    this.productSizes.push(sizeGroup);
  }

  onFileSelected(event: Event): void {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (file) {
      this.productForm.patchValue({ thumbnail: file });
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = (e) => {
        this.selectedImage = e.target!.result;
      };
    }
  }

  removeProductSize(index: number) {
    this.productSizes.removeAt(index);
  }

  onFilesSelected(event: Event): void {
    const files = (event.target as HTMLInputElement).files;
    if (files) {
      for (let i = 0; i < files.length; i++) {
        const file = files[i];
        const reader = new FileReader();
        reader.onload = (e) => {
          const imageUrl = e.target!.result as string;
          this.detailImages.push(
            new FormControl({
              file: file,
              imageUrl: imageUrl,
            })
          ); // Thêm ảnh vào FormArray
        };
        reader.readAsDataURL(file);
      }
    }
  }

  removeImage(index: number): void {
    this.detailImages.removeAt(index); // Xóa ảnh khỏi FormArray
  }

  createProduct() {
    const productDTO = {
      name: this.productForm.get('name')?.value,
      description: this.productForm.get('description')?.value,
      price: this.productForm.get('price')?.value,
      old_price: this.productForm.get('old_price')?.value,
      product_sizes: this.productForm.get('product_sizes')?.value,
      sku: this.productForm.get('sku')?.value,
      category_id: this.productForm.get('category_id')?.value,
    };
    const thumbnail = this.productForm.get('thumbnail')?.value;
    const detail_images = this.detailImages.controls.map((item) => {
      return item.value.file;
    });
    const formData = new FormData();
    formData.append(
      'product',
      new Blob([JSON.stringify(productDTO)], { type: 'application/json' })
    );
    if (thumbnail) {
      formData.append('thumbnail', thumbnail, thumbnail.name);
    }
    detail_images.forEach((file, index) => {
      formData.append('detail_images', file, `detail_images${index}`);
    });
    this.productService.createProduct(formData).subscribe({
      next: (response) => {
        console.log(response);
      },
      error: (error) => {
        console.log(error);
      },
    });
  }
}
