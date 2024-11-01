import { Injectable } from '@angular/core';
import { enviroment } from '../enviroments/enviroment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProductResponse } from '../responses/product/product.response';
import { ProductImageResponse } from '../responses/product/productImage.response';
import { Size } from '../responses/size.response';
import { BaseResponse } from '../responses/base.response';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private apiPageProducts = `${enviroment.apiBaseUrl}/public/products`;
  private apiGetProductById = `${enviroment.apiBaseUrl}/public/product`;
  private apiGetProductByCart = `${enviroment.apiBaseUrl}/public/product/orders`;
  private apiGetTop8ProductUpdated = `${enviroment.apiBaseUrl}/public/top8-products`;
  private apiGetTop4ProductUpdated = `${enviroment.apiBaseUrl}/public/top4-products`;
  private apiGetAllSize = `${enviroment.apiBaseUrl}/public/size`;
  private apiPostProduct =`${enviroment.apiBaseUrl}/product`;
  constructor(private http: HttpClient) {}
  getProducts(
    page: number,
    limit: number,
    keyword: string,
    categoryId: number
  ): Observable<BaseResponse<ProductResponse>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('limit', limit.toString())
      .set('keyword', keyword.toString())
      .set('categoryId', categoryId.toString());
    return this.http.get<BaseResponse<ProductResponse>>(this.apiPageProducts, { params });
  }
  getProduct(id: number): Observable<BaseResponse<ProductResponse>> {
    const params = new HttpParams().set('id', id);
    return this.http.get<BaseResponse<ProductResponse>>(  this.apiGetProductById, { params: params });
  }
  getProductByCart(ids: number[]): Observable<BaseResponse<ProductResponse>> {
    const params = new HttpParams().set('ids', ids.join(','));
    return this.http.get<BaseResponse<ProductResponse>>(
      this.apiGetProductByCart,
      {
        params,
      }
    );
  }
  getTop8ProductUpdated(): Observable<BaseResponse<ProductResponse>> {
    return this.http.get<BaseResponse<ProductResponse>>(this.apiGetTop8ProductUpdated);
  }
  
  getTop4ProductUpdated(): Observable<BaseResponse<ProductResponse>> {
    return this.http.get<BaseResponse<ProductResponse>>(this.apiGetTop4ProductUpdated);
  }
  getAllSize(): Observable<BaseResponse<Size>> {
    return this.http.get<BaseResponse<Size>>(this.apiGetAllSize);
  }

  createProduct(data: FormData): Observable<BaseResponse<ProductResponse>> {
    return this.http.post<BaseResponse<ProductResponse>>(this.apiPostProduct, data);
  }
  deleteProduct(id: number): Observable<any> {
    const params = new HttpParams().set('id', id);
    return this.http.delete<any>(this.apiPostProduct, { params: params });
  }
  updateProduct(data: FormData): Observable<BaseResponse<ProductResponse>> {
    return this.http.put<BaseResponse<ProductResponse>>(this.apiPostProduct, data);
  }
}
