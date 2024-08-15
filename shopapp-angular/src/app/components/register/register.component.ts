import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import {
  FormControl,
  FormGroup,
  Validators,
  FormsModule,
  ReactiveFormsModule,
  FormBuilder,
  AbstractControl,
  ValidationErrors,
} from '@angular/forms';
import { UserService } from '../../service/user.service';
import { RegisterDTO } from '../../dtos/user/register.dto';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
})
export class RegisterComponent {
  profileForm: FormGroup;
  registrationMessage: string = '';
  isError: boolean = false;

  constructor(private fb: FormBuilder, private userService: UserService) {
    this.profileForm = this.fb.group(
      {
        phone: ['', [Validators.required, Validators.minLength(6)]],
        password: ['', [Validators.required, Validators.minLength(5)]],
        retypePassword: ['', [Validators.required, Validators.minLength(5)]],
        fullName: ['', [Validators.required]],
        address: ['', [Validators.required]],
        isAccepted: [false, [Validators.requiredTrue]],
        dateOfBirth: ['', [Validators.required, this.ageValidator]],
      },
      { validator: this.passwordMatchValidator }
    );
    //   const headers = new HttpHeaders({'Content-Type' : 'application/json'});
    //   this.http.post(apiUrl,registerData,{headers})
    //          .subscribe({
    //             next: (respone: any) =>{
    //                if(respone && (respone.status === 200 || respone.status===201)){
    //                   this.ruoter.navigate(['/login']);
    //                }
    //             },
    //             complete:()=>{

    //             },
    //             error:(error: any) =>{
    //                alert(`Cannot register, error : ${error.error}`)
    //             }
    //          }

    //          )
  }
  onPhoneChange() {}
  submitHandler() {
    if (this.profileForm.valid) {
      const registerDTO: RegisterDTO = {
        fullname: this.profileForm.get('fullName')?.value,
        phone_number: this.profileForm.get('phone')?.value,
        address: this.profileForm.get('address')?.value,
        password: this.profileForm.get('password')?.value,
        retype_password: this.profileForm.get('retypePassword')?.value,
        date_of_birth: this.profileForm.get('dateOfBirth')?.value,
        facebook_account_id: 0,
        google_account_id: 0,
        role_id: 2,
      };

      this.userService.registerData(registerDTO).subscribe({
        next: (response: any) => {
          this.registrationMessage = response;
          this.isError = false;
          console.log(response);
        },
        error: (error: any) => {
          this.registrationMessage =
            error.error || 'An error occurred during registration';
          this.isError = true;
          console.log(error);
        },
        complete: () => {},
      });
    } else {
      this.registrationMessage = 'Please fill all required fields correctly.';
      this.isError = true;
    }
  }

  passwordMatchValidator(control: AbstractControl) {
    const password = control.get('password');
    const retypePassword = control.get('retypePassword')!;
    if (password && retypePassword && password.value !== retypePassword.value) {
      retypePassword.setErrors({ passwordMismatch: true });
    }
    return null;
  }
  ageValidator(control: AbstractControl): ValidationErrors | null {
    const dateOfBirth = control.value;
    if (!dateOfBirth) {
      return null;
    }
    const today = new Date();
    const dob = new Date(dateOfBirth);
    if (isNaN(dob.getTime())) {
      return { invalidDate: true };
    }
    let age = today.getFullYear() - dob.getFullYear();
    const monthDifference = today.getMonth() - dob.getMonth();
    if (
      monthDifference < 0 ||
      (monthDifference === 0 && today.getDate() < dob.getDate())
    ) {
      age--;
    }
    return age < 8 ? { ageUnder8: true } : null;
  }

  get phone() {
    return this.profileForm.get('phone')!;
  }
  get password() {
    return this.profileForm.get('password')!;
  }
  get retypePassword() {
    return this.profileForm.get('retypePassword')!;
  }
  get fullName() {
    return this.profileForm.get('fullName')!;
  }
  get address() {
    return this.profileForm.get('address')!;
  }
  get dateOfBirth() {
    return this.profileForm.get('dateOfBirth')!;
  }
  get isAccepted() {
    return this.profileForm.get('isAccepted')!;
  }
}
