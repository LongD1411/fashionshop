import { Injectable } from '@angular/core';
import { OrderDTO } from '../dtos/order/order.dto';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { enviroment } from '../enviroments/enviroment';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  private apiOrder = `${enviroment.apiBaseUrl}/orders`;
  private apiOrderDetail = `${enviroment.apiBaseUrl}/orders`;
  private apiConfig = {
    headers: this.createHeader(),
  };
  constructor(private http: HttpClient) {}

  private createHeader(): HttpHeaders {
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept-Language': 'vi',
    });
  }
  oder(orderDTO: OrderDTO): Observable<any> {
    return this.http.post(this.apiOrder, orderDTO, {
      headers: this.apiConfig.headers,
    });
  }
  getOrderDetail(orderId: string): Observable<any> {
    return this.http.get(`${this.apiOrderDetail}/${orderId}`,{headers:this.apiConfig.headers});
  }
}
