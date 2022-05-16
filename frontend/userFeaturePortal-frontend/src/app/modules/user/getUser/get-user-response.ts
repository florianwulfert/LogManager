import {UserDto} from "../../users/getUsers/user.dto";

export interface GetUserResponse {
  result: UserDto
}

export interface GetUserErrorResponse {
  error: string;
}
