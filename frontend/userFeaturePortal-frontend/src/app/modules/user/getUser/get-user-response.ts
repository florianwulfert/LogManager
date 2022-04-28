import {UserDto} from 'src/app/modules/user/getUser/user.dto'

export interface GetUserResponse {
  result: UserDto[];
  returnMessage: string;
}

export interface GetUserErrorResponse {
  error: string;
}
