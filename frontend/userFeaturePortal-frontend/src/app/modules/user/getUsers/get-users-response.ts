import {UserDto} from 'src/app/modules/user/getUsers/user.dto'

export interface GetUsersResponse {
  result: UserDto[];
  returnMessage: string;
}

export interface GetUsersErrorResponse {
  error: string;
}
