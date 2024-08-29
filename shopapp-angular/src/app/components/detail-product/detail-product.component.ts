import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ProductImageResponse } from '../../responses/product/productImage.response';
import { ProductService } from '../../service/product.service';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { ProductResponse } from '../../responses/product/product.response';
import { enviroment } from '../../enviroments/enviroment';
import { CommonModule } from '@angular/common';
import { CartService } from '../../service/cart.service';
import { Size } from '../../responses/size.response';
import { SweetAlertService } from '../../service/sweet-alert.service';
@Component({
  selector: 'app-detail-product',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './detail-product.component.html',
})
export class DetailProductComponent implements OnInit {
  productId: string | null = null;
  product: ProductResponse | null = null;
  quantity: number = 1;
  top4ProductUpdated: ProductResponse[] = [];
  selectedSizeId: number | null = null;
  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private router: Router,
    private cartService: CartService,
    private alert: SweetAlertService
  ) {}
  ngOnInit(): void {
    window.scroll(0, 0);
    this.route.paramMap.subscribe((params) => {
      this.productId = params.get('id');
    });
    if (this.productId) {
      this.productService.getProduct(this.productId).subscribe({
        next: (response) => {
          this.product = response;
          if (this.product?.thumbnail) {
            this.product.thumbnail = `${enviroment.apiImage}/${this.product?.thumbnail}`;
          }
          // if (this.product?.sizes) {
          //   this.sortSizes(this.product.sizes);
          // }
        },
        error: (error) => {
          console.log(error);
        },
        complete: () => {},
      });
    }
    this.productService.getTop4ProductUpdated().subscribe({
      next: (response) => {
        this.top4ProductUpdated = response;
        this.top4ProductUpdated.map((product) => {
          product.thumbnail = `${enviroment.apiImage}/${product.thumbnail}`;
        });
      },
      error: (error) => {
        console.log(error);
      },
      complete: () => {},
    });
  }
  sortSizes(sizes: Size[]): void {
    sizes.sort((a, b) => {
      if (a.sizeName < b.sizeName) {
        return -1;
      } else if (a.sizeName > b.sizeName) {
        return 1;
      } else {
        return 0;
      }
    });
  }
  quantityUp() {
    this.quantity += 1;
  }
  quantityDown() {
    if (this.quantity >= 2) {
      this.quantity -= 1;
    }
  }
  viewProductDetails(productId: number) {
    this.router
      .navigate(['/chi-tiet-san-pham', productId])
      .then(() => window.scroll(0, 0));
  }
  addToCart(id: string) {
    const ID = parseInt(id);
    if (ID) {
      this.cartService.setCart(ID, this.selectedSizeId!, 1);
    }
    this.alert.showSuccess('Thêm thành công');
  }
  onSizeChange(sizeId: number): void {
    this.selectedSizeId = sizeId;
  }
  // product: ProductResponse | undefined;
  // currentIndex: number = 0;
  // quantity: number = 1;
  // constructor(
  //   private productService: ProductService,
  //   private route: ActivatedRoute,
  //   private cartService: CartService
  // ) {}
  // ngOnInit() {
  //   const id = this.route.snapshot.paramMap.get('id');
  //   if (id != null) {
  //     this.productService.getProduct(id).subscribe({
  //       next: (response: any) => {
  //         if (response.product_images && response.product_images.length > 0) {
  //           response.product_images.forEach(
  //             (productImage: ProductImageResponse) => {
  //               productImage.thumbnail_url = `${enviroment.apiBaseUrl}/products/images/${productImage.thumbnail_url}`;
  //             }
  //           );
  //         }
  //         this.product = response;
  //         console.log(this.product);
  //         this.showImage(0);
  //       },
  //       complete: () => {},
  //       error: (error: any) => {
  //         console.log(error);
  //       },
  //     });
  //   }
  // }
  // showImage(index: number) {
  //   if (
  //     this.product &&
  //     this.product.product_images &&
  //     this.product.product_images.length > 0
  //   ) {
  //     if (index >= this.product.product_images.length) {
  //       index = 0;
  //     }
  //     if (index < 0) {
  //       index = this.product.product_images.length - 1;
  //     }
  //   }
  //   this.currentIndex = index;
  // }
  // nextImage() {
  //   this.currentIndex += 1;
  //   this.showImage(this.currentIndex);
  // }
  // previousImage() {
  //   this.currentIndex -= 1;
  //   this.showImage(this.currentIndex);
  // }
  // clickImage(index: number) {
  //   this.showImage(index);
  // }
  // upQuantity() {
  //   this.quantity += 1;
  // }
  // downQuantity() {
  //   if (this.quantity > 1) {
  //     this.quantity -= 1;
  //   }
  // }
  // addToCart() {
  //   if (this.product?.id && this.quantity) {
  //     this.cartService.setCart(this.product?.id, this.quantity);
  //   }
  // }
}
