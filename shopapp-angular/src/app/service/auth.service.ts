import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { enviroment } from '../enviroments/enviroment';
@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiIsTokenValid = `${enviroment.apiBaseUrl}/auth/token/validate`;
  private apiIsUserValid = `${enviroment.apiBaseUrl}/auth/user/validate`;
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
    return this.http.get(this.apiIsTokenValid, { headers: this.apiConfig.headers});
  }
  isUserValidate() {
    return this.http.get(this.apiIsUserValid, { headers: this.apiConfig.headers});
  }
}
