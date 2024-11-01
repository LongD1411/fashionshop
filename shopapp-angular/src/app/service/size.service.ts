import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Size } from '../responses/size.response';
import { HttpClient, HttpParams } from '@angular/common/http';
import { enviroment } from '../enviroments/enviroment';
import { BaseResponse } from '../responses/base.response';

@Injectable({
  providedIn: 'root',
})
export class SizeService {
  apiGetAllSize = `${enviroment.apiBaseUrl}/public/size`;
  apiCreateSize = `${enviroment.apiBaseUrl}/size`; //update cung dung dc
  apiDeleteSize = `${enviroment.apiBaseUrl}/size`;
  constructor(private http: HttpClient) {}
  getAllSize(): Observable<BaseResponse<Size>> {
    return this.http.get<BaseResponse<Size>>(this.apiGetAllSize);
  }
  getSize(id: number): Observable<BaseResponse<Size>> {
    const param = new HttpParams().set('id', id.toString());
    return this.http.get<BaseResponse<Size>>(this.apiGetAllSize, {
      params: param,
    });
  }
  createSize(sizeDTO: any): Observable<Size> {
    return this.http.post<Size>(this.apiCreateSize, sizeDTO);
  }
  removeSize(id: number): Observable<any> {
    const param = new HttpParams().set('id', id);
    return this.http.delete(this.apiDeleteSize, { params: param });
  }
  updateSize(sizeDTO: any): Observable<Size> {
    return this.http.put<Size>(this.apiCreateSize, sizeDTO);
  }
}
