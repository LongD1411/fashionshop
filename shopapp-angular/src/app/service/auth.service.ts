import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { enviroment } from '../enviroments/enviroment';
import { BaseResponse } from '../responses/base.response';
import { AuthResponse } from '../responses/auth.response';
import { LoginDTO } from '../dtos/user/login.dto';
import { Observable } from 'rxjs';
import { RegisterDTO } from '../dtos/user/register.dto';
import { AuthDTO } from '../dtos/auth.dto';
@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiIsTokenValid = `${enviroment.apiBaseUrl}/auth/token/validate`;
  private apiIsUserValid = `${enviroment.apiBaseUrl}/auth/user/validate`;
  private apiLogin = `${enviroment.apiBaseUrl}/auth/login`;
  private apiLogout = `${enviroment.apiBaseUrl}/auth/logout`;
  private apiRegister = `${enviroment.apiBaseUrl}/auth/register`;
  private apiRefershToken = `${enviroment.apiBaseUrl}/auth/refresh`;
  private apiConfig = {
    headers: this.createHeader(),
  };
  constructor(private http: HttpClient) {}

  private createHeader(): HttpHeaders {
    return new HttpHeaders({
      'Content-Type': 'application/json',
    });
  }
  isTokenValidate() {
    return this.http.get(this.apiIsTokenValid, {
      headers: this.apiConfig.headers,
    });
  }
  isUserValidate() {
    return this.http.get(this.apiIsUserValid, {
      headers: this.apiConfig.headers,
    });
  }
  login(loginDTO: LoginDTO): Observable<BaseResponse<AuthResponse>> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<BaseResponse<AuthResponse>>(this.apiLogin, loginDTO, {
      headers: this.apiConfig.headers,
    });
  }
  logout(loginDTO: AuthDTO): Observable<BaseResponse<AuthResponse>> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<BaseResponse<AuthResponse>>(
      this.apiLogout,
      loginDTO,
      {
        headers: this.apiConfig.headers,
      }
    );
  }
  register(registerData: RegisterDTO): Observable<BaseResponse<AuthResponse>> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<BaseResponse<AuthResponse>>(
      this.apiRegister,
      registerData,
      {
        headers: this.apiConfig.headers,
      }
    );
  }
  getTokenRefresh(data: AuthDTO): Observable<BaseResponse<AuthResponse>> {
    return this.http.post<BaseResponse<AuthResponse>>(
      this.apiRefershToken,
      data,
      { headers: { 'Content-Type': 'application/json' } }
    );
  }
}
