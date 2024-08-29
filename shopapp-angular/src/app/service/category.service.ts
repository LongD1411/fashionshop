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
  getCategories(): Observable<CategoryResponse[]> {
    const apiGetCategories = `${enviroment.apiBaseUrl}/categories`;
    return this.http.get<CategoryResponse[]>(apiGetCategories);
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
