import {UserDto} from "../getUsers/user.dto";

export interface AddUserResponse {
  result: UserDto[];
  returnMessage: string
}

export interface AddUserErrorResponse {
  error: string;
}
