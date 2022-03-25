import {UserDto} from "../getUser/dto/user.dto";

export interface DeleteUsersResponse {
  result: UserDto[];
}

export interface DeleteUsersErrorResponse {
  error: string;
}
