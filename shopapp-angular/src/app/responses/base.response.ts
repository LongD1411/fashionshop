export interface BaseResponse<T> {
  code: number;
  results: T[];
  result: T;
  message: string;
  totalPage: number;
  totalItem: number;
}
