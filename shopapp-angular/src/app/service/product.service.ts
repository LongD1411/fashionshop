import { Injectable } from '@angular/core';
import { enviroment } from '../enviroments/enviroment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProductResponse } from '../responses/product/product.response';
import { ProductImageResponse } from '../responses/product/productImage.response';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private apiGetProducts = `${enviroment.apiBaseUrl}/products`;
  private apiGetTop6ProductUpdated = `${enviroment.apiBaseUrl}/products/home`;
  constructor(private http: HttpClient) {}
  getProducts(
    page: number,
    limit: number,
    keyword: string,
    categoryId: number
  ): Observable<ProductResponse[]> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('limit', limit.toString())
      .set('keyword', keyword.toString())
      .set('categoryId', categoryId.toString());
    return this.http.get<ProductResponse[]>(this.apiGetProducts, { params });
  }
  getProduct(id: string): Observable<any> {
    return this.http.get(`${this.apiGetProducts}/${id}`);
  }
  getProductOrders(ids: number[]): Observable<ProductResponse[]> {
    const params = new HttpParams().set('ids', ids.join(','));
    return this.http.get<ProductResponse[]> (
      `${enviroment.apiBaseUrl}/products/orders`,
      {
        params,
      }
    );
  }
  getTop6ProductUpdated():Observable<ProductResponse[]>{
    return this.http.get<ProductResponse[]>(this.apiGetTop6ProductUpdated)
  }
}
