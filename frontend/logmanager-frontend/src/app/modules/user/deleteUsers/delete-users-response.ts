import {UserDto} from "../getUser/user.dto";

export interface DeleteUsersResponse {
  result: UserDto[];
}

export interface DeleteUsersErrorResponse {
  error: string;
}
