import {UserDto} from "../../users/getUsers/user.dto";

export interface GetUserResponse {
  user: UserDto
}

export interface GetUserErrorResponse {
  error: string;
}
