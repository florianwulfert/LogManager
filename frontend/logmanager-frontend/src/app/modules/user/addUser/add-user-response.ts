import {UserDto} from "../getUser/dto/user.dto";

export interface AddUserResponse {
  result: UserDto[];
}

export interface AddUserErrorResponse {
  error: string;
}
