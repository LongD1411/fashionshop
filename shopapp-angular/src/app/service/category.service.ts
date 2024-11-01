import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CategoryResponse } from '../responses/category/category.respones';
import { enviroment } from '../enviroments/enviroment';
import { BannerResponse } from '../responses/banner.respose';
import { BaseResponse } from '../responses/base.response';

@Injectable({
  providedIn: 'root',
})
export class CategoryService {
  constructor(private http: HttpClient) {}
  apiBanner = `${enviroment.apiBaseUrl}/banner`;
  apiGetBanner = `${enviroment.apiBaseUrl}/public/banner`;
  apiCategories = `${enviroment.apiBaseUrl}/categories`;
  apiGetCategories = `${enviroment.apiBaseUrl}/public/categories`;
  getCategories(): Observable<BaseResponse<CategoryResponse>> {
    return this.http.get<BaseResponse<CategoryResponse>>(this.apiGetCategories);
  }
  getCategory(id: number): Observable<BaseResponse<CategoryResponse>> {
    const param = new HttpParams().set('id', id);
    return this.http.get<BaseResponse<CategoryResponse>>(this.apiGetCategories, {
      params: param,
    });
  }
  deleteCategory(id: number): Observable<any> {
    const param = new HttpParams().set('id', id);
    return this.http.delete<any>(this.apiCategories, { params: param });
  }
  createCategory(data: FormData): Observable<BaseResponse<CategoryResponse>> {
    return this.http.post<BaseResponse<CategoryResponse>>(this.apiCategories, data);
  }
  updateCategory(data: FormData): Observable<BaseResponse<CategoryResponse>> {
    return this.http.put<BaseResponse<CategoryResponse>>(this.apiCategories, data);
  }
  getAllBanners(): Observable<BaseResponse<BannerResponse>> {
    return this.http.get<BaseResponse<BannerResponse>>(this.apiGetBanner);
  }
  getBanner(id: number):Observable<BaseResponse<BannerResponse>> {
    const param = new HttpParams().set('id', id);
    return this.http.get<BaseResponse<BannerResponse>>(this.apiGetBanner, { params: param });
  }
  createBanner(data: FormData): Observable<BaseResponse<BannerResponse>> {
    return this.http.post<BaseResponse<BannerResponse>>(this.apiBanner, data);
  }
  deleteBanner(id: number): Observable<any> {
    const param = new HttpParams().set('id', id);
    return this.http.delete<any>(this.apiBanner, { params: param });
  }
  updateBanner(data: FormData): Observable<BannerResponse> {
    return this.http.put<BannerResponse>(this.apiBanner, data);
  }
}
