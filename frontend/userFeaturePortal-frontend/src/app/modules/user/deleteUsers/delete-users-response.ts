import {UserDto} from "../getUsers/user.dto";

export interface DeleteUsersResponse {
  result: UserDto[]
  returnMessage: string
}

export interface DeleteUsersErrorResponse {
  error: string
}
