import {UserDto} from "../dto/user.dto";

export const USER_FEATURE_NAME = 'user';

export interface UserState {
  userList: UserDto[];
  error: string;
}

export const USER_INITIAL_STATE: UserState = {
  userList: [],
  error: '',
};
