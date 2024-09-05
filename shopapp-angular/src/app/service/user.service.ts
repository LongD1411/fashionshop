import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { RegisterDTO } from '../dtos/user/register.dto';
import { LoginDTO } from '../dtos/user/login.dto';
import { enviroment } from '../enviroments/enviroment';
import { Router } from '@angular/router';
import { UserResponse } from '../responses/user/user.response';
import { UpdateUserDTO } from '../dtos/user/update.user.dto';
@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiRegister = `${enviroment.apiBaseUrl}/users/register`;
  private apiLogin = `${enviroment.apiBaseUrl}/users/login`;
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
      'Accept-Language': 'vi',
    });
  }

  registerData(registerData: RegisterDTO): Observable<string> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post(this.apiRegister, registerData, {
      headers: this.apiConfig.headers,
      responseType: 'text',
    });
  }

  login(loginDTO: LoginDTO): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post(this.apiLogin, loginDTO, {
      headers: this.apiConfig.headers,
    });
  }
  logout() {
    localStorage.removeItem('access_token');
    this.router.navigate(['dang-nhap']);
  }
  getUserDetail(): Observable<any> {
    return this.http.post(this.apiUserDetail, null, {
      headers: this.apiConfig.headers,
    });
  }
  updatedUser(user: UpdateUserDTO): Observable<any> {
    return this.http.put(this.apiUserUpdate, user, {
      headers: this.apiConfig.headers,
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
