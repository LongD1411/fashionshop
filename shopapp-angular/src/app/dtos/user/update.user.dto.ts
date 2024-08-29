export class UpdateUserDTO {
    fullname: string;
    phone_number: string;
    address: string;
    date_of_birth: Date;
    constructor(data: any) {
      this.fullname = data.full_name;
      this.address = data.address;
      this.date_of_birth = data.date_of_birth;
      this.phone_number = data.phone_number;
    }
  }
  