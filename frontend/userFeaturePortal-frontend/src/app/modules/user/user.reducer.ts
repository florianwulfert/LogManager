import {createReducer, on} from "@ngrx/store";
import {USER_GET_INITIAL_STATE, UserState} from "./user.state";
import {
  addUserResponseAction,
  deleteUserResponseAction,
  deleteUsersResponseAction,
  getUsersResponseAction
} from "./user.actions";
import {GetUsersResponse} from "./getUsers/get-users-response";

const handleUserResponse = (state: UserState, resp: GetUsersResponse): UserState => {
  return {
    ...state,
    userList: resp.result,
  };
};

export const UserReducer = createReducer(
  USER_GET_INITIAL_STATE,
  on(getUsersResponseAction, handleUserResponse),
  on(deleteUserResponseAction, handleUserResponse),
  on(deleteUsersResponseAction, handleUserResponse),
  on(addUserResponseAction, handleUserResponse),
);
