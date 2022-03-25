import {UserDto} from "../getUser/user.dto";

export interface DeleteUserResponse {
  result: UserDto[];
}

export interface DeleteUserErrorResponse {
  error: string;
}
