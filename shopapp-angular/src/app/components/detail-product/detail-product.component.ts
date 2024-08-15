import { Component, OnInit } from '@angular/core';
import { ProductImageResponse } from '../../responses/product/productImage.response';
import { ProductService } from '../../service/product.service';
import { ActivatedRoute } from '@angular/router';
import { ProductResponse } from '../../responses/product/product.response';
import { enviroment } from '../../enviroments/enviroment';
import { CommonModule } from '@angular/common';
import { CartService } from '../../service/cart.service';
@Component({
  selector: 'app-detail-product',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './detail-product.component.html',
})
export class DetailProductComponent {
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
