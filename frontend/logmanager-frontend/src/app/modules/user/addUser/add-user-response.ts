import {UserDto} from "../getUser/user.dto";

export interface AddUserResponse {
  result: UserDto[];
}

export interface AddUserErrorResponse {
  error: string;
}
