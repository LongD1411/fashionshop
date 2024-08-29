import { Component, OnInit } from '@angular/core';
import { UserResponse } from '../../responses/user/user.response';
import { UserService } from '../../service/user.service';
import { CommonModule, DatePipe } from '@angular/common';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Route, Router, RouterLink } from '@angular/router';
import { SweetAlertService } from '../../service/sweet-alert.service';
import Swal from 'sweetalert2';
import { UpdateUserDTO } from '../../dtos/user/update.user.dto';

@Component({
  selector: 'app-user.detail',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './user.detail.component.html',
  styleUrl: './user.detail.component.css',
})
export class UserDetailComponent implements OnInit {
  userResponse: UserResponse | undefined;
  userDetailForm!: FormGroup;
  formattedDate!: string;
  constructor(
    private userService: UserService,
    private fb: FormBuilder,
    private router: Router,
    private alert: SweetAlertService
  ) {
    this.userDetailForm = this.fb.group({
      full_name: [
        '',
        [
          Validators.required,
          Validators.minLength(5),
          Validators.maxLength(50),
        ],
      ],
      date_of_birth: ['', Validators.required],
      address: ['', Validators.required],
      phone_number: ['', Validators.required],
    });
  }
  ngOnInit(): void {
    this.userService.getUserDetail().subscribe({
      next: (response) => {
        if (response) {
          this.userResponse = response;
          const dob = new Date(this.userResponse!.date_of_birth);
          this.formattedDate = this.getFormattedDate(dob);
          this.userDetailForm.patchValue({
            full_name: this.userResponse?.full_name,
            address: this.userResponse?.address,
            date_of_birth: this.formattedDate,
            phone_number: this.userResponse?.phone_number,
          });
        }
      },
      error: (error) => {
        this.router.navigate(['/dang-nhap']);
      },
      complete: () => {},
    });
  }
  getFormattedDate(date: Date): string {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
  }
  handleSubmit() {
    if (this.userDetailForm.valid) {
      const userDto: UpdateUserDTO = {
        fullname: this.userDetailForm.get('full_name')?.value,
        phone_number: this.userDetailForm.get('phone_number')?.value,
        address: this.userDetailForm.get('address')?.value,
        date_of_birth: this.userDetailForm.get('date_of_birth')?.value,
      };
      this.alert
        .showConfirm(
          'Bạn có chắc chắn?',
          'Bạn không thể hoàn tác thao tác này!'
        )
        .then((result) => {
          if (result.isConfirmed) {
            this.userService.updatedUser(userDto).subscribe({
              next: (response) => {
                if (response) {
                  this.userResponse = response;
                  const dob = new Date(this.userResponse!.date_of_birth);
                  this.formattedDate = this.getFormattedDate(dob);
                  this.userDetailForm.patchValue({
                    full_name: this.userResponse?.full_name,
                    address: this.userResponse?.address,
                    date_of_birth: this.formattedDate,
                    phone_number: this.userResponse?.phone_number,
                  });
                }
                this.alert.showSuccess('Cập nhật thông tin thành công');
              },
              error: (error) => {
                this.router.navigate(['/dang-nhap']);
                console.log(error);
                if (error.error) {
                  this.alert.showError(error.error.message);
                }
              },
              complete: () => {},
            });
          }
        });
    }
  }
}
