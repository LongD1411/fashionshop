import { Injectable } from '@angular/core';
import { OrderDTO } from '../dtos/order/order.dto';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { enviroment } from '../enviroments/enviroment';
import { BaseResponse } from '../responses/base.response';
import { OrderResponse } from '../responses/order/order.response';
import { OrderDetailResponse } from '../responses/order/order.detail.response';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  private apiOrder = `${enviroment.apiBaseUrl}/order`;
  private apiAllOrder = `${enviroment.apiBaseUrl}/order/user`;
  private apiAllOrderAdmin = `${enviroment.apiBaseUrl}/orders`;
  private apiOrderDetail = `${enviroment.apiBaseUrl}/order-details/order`;
  private apiConfig = {
    headers: this.createHeader(),
  };
  constructor(private http: HttpClient) {}

  private createHeader(): HttpHeaders {
    return new HttpHeaders({
      'Content-Type': 'application/json',
    });
  }
  oder(orderDTO: OrderDTO): Observable<BaseResponse<OrderResponse>> {
    return this.http.post<BaseResponse<OrderResponse>>(
      this.apiOrder,
      orderDTO,
      {
        headers: this.apiConfig.headers,
      }
    );
  }
  getOrderDetail(
    orderId: string
  ): Observable<BaseResponse<OrderDetailResponse>> {
    const param = new HttpParams().set('id', orderId);
    return this.http.get<BaseResponse<OrderDetailResponse>>(
      this.apiOrderDetail,
      {
        params: param,
        headers: this.apiConfig.headers,
      }
    );
  }
  getOrderSuccess(orderId: string): Observable<BaseResponse<OrderResponse>> {
    const param = new HttpParams().set('id', orderId);
    return this.http.get<BaseResponse<OrderResponse>>(this.apiOrder, {
      params: param,
    });
  }
  getAllOrder(): Observable<BaseResponse<OrderResponse>> {
    return this.http.get<BaseResponse<OrderResponse>>(this.apiAllOrder);
  }
    updateOrder(id: string, status:string): Observable<BaseResponse<OrderResponse>> {
      const params = new HttpParams()
        .set('id', id)
        .set('status', status);
      return this.http.put<BaseResponse<OrderResponse>>(this.apiOrder,null,{params});
    }
  getAllOrderAdmin(
    page: number,
    limit: number,
    keyword: string
  ): Observable<BaseResponse<OrderResponse>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('limit', limit.toString())
      .set('keyword', keyword.toString());
    return this.http.get<BaseResponse<OrderResponse>>(this.apiAllOrderAdmin, {
      params,
    });
  }
}
