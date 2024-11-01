export class RegisterDTO {
  fullname: string;
  phone_number: string;
  address: string;
  password: string;
  date_of_birth: Date;
  facebook_account_id: string;
  google_account_id: string;
  constructor(data: any) {
    this.fullname = data.full_name;
    this.address = data.address;
    this.date_of_birth = data.date_of_birth;
    this.password = data.password;
    this.phone_number = data.phone_number;
    this.facebook_account_id = data.facebook_account_id;
    this.google_account_id = data.google_account_id;
  }
}
