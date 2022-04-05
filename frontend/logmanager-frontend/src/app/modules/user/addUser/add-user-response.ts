import {UserDto} from "../getUser/user.dto";

export interface AddUserResponse {
  result: UserDto[];
  returnMessage: string
}

export interface AddUserErrorResponse {
  error: string;
}
