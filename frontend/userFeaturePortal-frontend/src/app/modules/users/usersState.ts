import {UserDto} from "./getUsers/user.dto";

export const USERS_FEATURE_NAME = 'users';

export interface UsersState {
  userList: UserDto[];
}

export const USERS_GET_INITIAL_STATE: UsersState = {
  userList: [],
};
