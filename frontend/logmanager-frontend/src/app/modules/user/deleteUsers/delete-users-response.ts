import {UserDto} from "../getUser/user.dto";

export interface DeleteUsersResponse {
  result: UserDto[]
  returnMessage: string
}

export interface DeleteUsersErrorResponse {
  error: string
}
