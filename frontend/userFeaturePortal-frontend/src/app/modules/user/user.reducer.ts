import {createReducer, on} from "@ngrx/store";
import {USERS_GET_INITIAL_STATE, UsersState} from "./usersState";
import {
  addUserResponseAction,
  deleteUserResponseAction,
  deleteUsersResponseAction,
  getUsersResponseAction
} from "./user.actions";
import {GetUsersResponse} from "./getUsers/get-users-response";

const handleUserResponse = (state: UsersState, resp: GetUsersResponse): UsersState => {
  return {
    ...state,
    userList: resp.result,
  };
};

export const UserReducer = createReducer(
  USERS_GET_INITIAL_STATE,
  on(getUsersResponseAction, handleUserResponse),
  on(deleteUserResponseAction, handleUserResponse),
  on(deleteUsersResponseAction, handleUserResponse),
  on(addUserResponseAction, handleUserResponse),
);
