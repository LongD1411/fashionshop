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

@Component({
  selector: 'app-user.detail',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './user.detail.component.html',
  styleUrl: './user.detail.component.css',
})
export class UserDetailComponent implements OnInit {
  userResponse: UserResponse | undefined;
  userDetailForm!: FormGroup;
  constructor(private userService: UserService, private fb: FormBuilder) {
    this.userDetailForm = this.fb.group({
      full_name: [
        '',
        [
          Validators.required,
          Validators.minLength(5),
          Validators.maxLength(50),
        ],
      ],
      // date_of_birth: ['', Validators.required],
      address: ['', Validators.required],
      phone_number: [
        '',
        Validators.required,
        Validators.minLength(6),
        Validators.maxLength(15),
      ],
    });
  }
  ngOnInit(): void {
    debugger;
    this.userService.getUserDetail().subscribe({
      next: (response) => {
        if (response) {
          this.userResponse = response;
          // this.userResponse!.date_of_birth = new Date(
          //   this.userResponse!.date_of_birth
          // );
          // console.log(this.userResponse?.date_of_birth.toLocaleDateString());
          this.userDetailForm.patchValue({
            full_name: this.userResponse?.full_name,
            address: this.userResponse?.address,
            // date_of_birth: this.userResponse?.date_of_birth,
            phone_number: this.userResponse?.phone_number,
          });
        }
      },
      error: (error) => {
        console.log(error);
      },
      complete: () => {},
    });
  }
}
