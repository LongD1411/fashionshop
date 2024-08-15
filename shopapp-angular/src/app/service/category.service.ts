import { HttpClient } from '@angular/common/http';
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

  getCategories(): Observable<CategoryResponse[]> {
    const apiGetCategories = `${enviroment.apiBaseUrl}/categories`;
    return this.http.get<CategoryResponse[]>(apiGetCategories);
  }
  getBanners(): Observable<BannerResponse[]> {
    const apiGetCategories = `${enviroment.apiBaseUrl}/banner`;
    return this.http.get<BannerResponse[]>(apiGetCategories);
  }
}
