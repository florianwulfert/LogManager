import {UserDto} from 'src/app/modules/users/getUsers/user.dto'

export interface GetUsersResponse {
  result: UserDto[];
  returnMessage: string;
}

export interface GetUsersErrorResponse {
  error: string;
}
