export class RegisterDTO {
  fullname: string;
  phone_number: string;
  address: string;
  password: string;
  retype_password: string;
  date_of_birth: Date;
  facebook_account_id: number;
  google_account_id: number;
  role_id: number;
  constructor(data: any) {
    this.fullname = data.full_name;
    this.address = data.address;
    this.date_of_birth = data.date_of_birth;
    this.password = data.password;
    this.retype_password = data.retype_password;
    this.phone_number = data.phone_number;
    this.facebook_account_id = 0;
    this.google_account_id = 0;
    this.role_id = 2;
  }
}
