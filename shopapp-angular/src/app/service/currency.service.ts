import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class CurrencyService {
  // Hàm này dùng để format tiền tệ dạng VND
  formatCurrency(value: number): string {
    return value.toLocaleString('it-IT', {style : 'currency', currency : 'VND'});
  }
  // Hàm này dùng để loại bỏ dấu phân cách và đơn vị tiền tệ khi lưu vào DB
  parseCurrency(formattedValue: string): number {
    // Loại bỏ các dấu phân cách, đơn vị tiền tệ và chuyển về số
    return Number(formattedValue.replace(/\D/g, ''));
  }
}
