import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CategoryResponse } from '../responses/category/category.respones';
import { enviroment } from '../enviroments/enviroment';
import { BannerResponse } from '../responses/banner.respose';

@Injectable({
  providedIn: 'root',
})
export class CategoryService {
  constructor(private http: HttpClient) {}
  apiBanner = `${enviroment.apiBaseUrl}/banner`;
  apiCategories = `${enviroment.apiBaseUrl}/categories`;
  getCategories(): Observable<CategoryResponse[]> {
    return this.http.get<CategoryResponse[]>(this.apiCategories);
  }
  getCategory(id: number): Observable<CategoryResponse> {
    const param = new HttpParams().set('id', id);
    return this.http.get<CategoryResponse>(this.apiCategories, {
      params: param,
    });
  }
  deleteCategory(id: number): Observable<any> {
    const param = new HttpParams().set('id', id);
    return this.http.delete<any>(this.apiCategories, { params: param });
  }
  createCategory(data: FormData): Observable<CategoryResponse> {
    return this.http.post<CategoryResponse>(this.apiCategories, data);
  }
  updateCategory(data: FormData): Observable<CategoryResponse> {
    return this.http.put<CategoryResponse>(this.apiCategories, data);
  }
  getAllBanners(): Observable<BannerResponse[]> {
    return this.http.get<BannerResponse[]>(this.apiBanner);
  }
  getBanner(id: number): Observable<BannerResponse> {
    const param = new HttpParams().set('id', id);
    return this.http.get<BannerResponse>(this.apiBanner, { params: param });
  }
  createBanner(data: FormData): Observable<BannerResponse> {
    return this.http.post<BannerResponse>(this.apiBanner, data);
  }
  deleteBanner(id: number): Observable<any> {
    const param = new HttpParams().set('id', id);
    return this.http.delete<any>(this.apiBanner, { params: param });
  }
  updateBanner(data: FormData): Observable<BannerResponse> {
    return this.http.put<BannerResponse>(this.apiBanner, data);
  }
}
