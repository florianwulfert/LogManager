import {UserDto} from 'src/app/modules/user/getUser/dto/user.dto'

export interface GetUserResponse {
  result: UserDto[];
}

export interface GetUserErrorResponse {
  error: string;
}
