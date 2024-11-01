import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { RegisterDTO } from '../dtos/user/register.dto';
import { LoginDTO } from '../dtos/user/login.dto';
import { enviroment } from '../enviroments/enviroment';
import { Router } from '@angular/router';
import { UserResponse } from '../responses/user/user.response';
import { UpdateUserDTO } from '../dtos/user/update.user.dto';
import { BaseResponse } from '../responses/base.response';
@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiUserDetail = `${enviroment.apiBaseUrl}/users/detail`;
  private apiUserUpdate = `${enviroment.apiBaseUrl}/users/update`;
  private apiGetAllUser = `${enviroment.apiBaseUrl}/users/all`;
  private apiBanUser = `${enviroment.apiBaseUrl}/users/ban`;
  private apiUnbanUser = `${enviroment.apiBaseUrl}/users/unban`;
  private apiConfig = {
    headers: this.createHeader(),
  };
  constructor(private http: HttpClient, private router: Router) {}

  private createHeader(): HttpHeaders {
    return new HttpHeaders({
      'Content-Type': 'application/json',
    });
  }

  getUserDetail(): Observable<BaseResponse<UserResponse>> {
    return this.http.get<BaseResponse<UserResponse>>(this.apiUserDetail);
  }
  updatedUser(user: UpdateUserDTO): Observable<BaseResponse<UserResponse>> {
    const params = new HttpParams().set('id', user.id);
    return this.http.put<BaseResponse<UserResponse>>(this.apiUserUpdate, user, {
      headers: this.apiConfig.headers,
      params: params,
    });
  }
  getAllUsers(page: number, limit: number, keyword: string): Observable<any> {
    const param = new HttpParams()
      .set('page', page)
      .set('limit', limit)
      .set('keyword', keyword);
    return this.http.get<any>(this.apiGetAllUser, { params: param });
  }
  ban(id: number): Observable<any> {
    const params = new HttpParams().set('id', id.toString());
    return this.http.put<any>(this.apiBanUser, null, { params: params });
  }
  unban(id: number): Observable<any> {
    const params = new HttpParams().set('id', id.toString());
    return this.http.put(this.apiUnbanUser, null, { params: params });
  }
}
