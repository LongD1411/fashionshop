import { Injectable } from '@angular/core';
import { enviroment } from '../enviroments/enviroment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProductResponse } from '../responses/product/product.response';
import { ProductImageResponse } from '../responses/product/productImage.response';
import { Size } from '../responses/size.response';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private apiProducts = `${enviroment.apiBaseUrl}/products`;
  private apiGetTop8ProductUpdated = `${enviroment.apiBaseUrl}/products/top8`;
  private apiGetTop4ProductUpdated = `${enviroment.apiBaseUrl}/products/top4`;
  private apiGetAllSize = `${enviroment.apiBaseUrl}/size`;
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
    return this.http.get<ProductResponse[]>(this.apiProducts, { params });
  }
  getProduct(id: string): Observable<any> {
    return this.http.get(`${this.apiProducts}/${id}`);
  }
  getProductOrders(ids: number[]): Observable<ProductResponse[]> {
    const params = new HttpParams().set('ids', ids.join(','));
    return this.http.get<ProductResponse[]>(
      `${enviroment.apiBaseUrl}/products/orders`,
      {
        params,
      }
    );
  }
  getTop8ProductUpdated(): Observable<ProductResponse[]> {
    return this.http.get<ProductResponse[]>(this.apiGetTop8ProductUpdated);
  }
  getTop4ProductUpdated(): Observable<ProductResponse[]> {
    return this.http.get<ProductResponse[]>(this.apiGetTop4ProductUpdated);
  }
  getAllSize(): Observable<Size[]> {
    return this.http.get<Size[]>(this.apiGetAllSize);
  }

  createProduct(data: FormData): Observable<any> {
    console.log(data);
    return this.http.post<any>(this.apiProducts, data);
  }
}
