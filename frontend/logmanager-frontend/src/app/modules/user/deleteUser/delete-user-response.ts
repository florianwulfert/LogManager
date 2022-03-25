import {UserDto} from "../getUser/dto/user.dto";

export interface DeleteUserResponse {
  result: UserDto[];
}

export interface DeleteUserErrorResponse {
  error: string;
}
