import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
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
}
