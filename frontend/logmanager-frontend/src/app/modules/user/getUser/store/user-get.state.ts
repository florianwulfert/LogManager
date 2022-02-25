import {UserDto} from "../dto/user.dto";

export const GET_USER_FEATURE_NAME = 'getUser';

export interface UserGetState {
  userList: UserDto[];
  error: string;
}

export const USER_GET_INITIAL_STATE: UserGetState = {
  userList: [],
  error: ''
};
