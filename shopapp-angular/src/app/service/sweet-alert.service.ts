import { Injectable } from '@angular/core';
import Swal from 'sweetalert2';

@Injectable({
  providedIn: 'root',
})
export class SweetAlertService {
  constructor() {}

  showSuccess(message: string) {
    return Swal.fire({
      text: message,
      icon: 'success',
      confirmButtonText: 'OK',
    });
  }

  showError(message: string, title: string = 'Lỗi') {
    Swal.fire({
      title: title,
      text: message,
      icon: 'error',
      confirmButtonText: 'OK',
    });
  }

  showConfirm(title: string, text: string) {
    return Swal.fire({
      title: title,
      text: text,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'OK',
      cancelButtonText: 'Hủy',
      reverseButtons: true,
    });
  }
}
