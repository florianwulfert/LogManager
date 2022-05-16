import {createReducer, on} from "@ngrx/store";
import {USERS_GET_INITIAL_STATE, UsersState} from "./usersState";
import {
  addUserResponseAction,
  deleteUserResponseAction,
  deleteUsersResponseAction,
  getUsersResponseAction
} from "./users.actions";
import {GetUsersResponse} from "./getUsers/get-users-response";

const handleUsersResponse = (state: UsersState, resp: GetUsersResponse): UsersState => {
  console.log(resp)
  return {
    ...state,
    userList: resp.result,
  };
};

export const UsersReducer = createReducer(
  USERS_GET_INITIAL_STATE,
  on(getUsersResponseAction, handleUsersResponse),
  on(deleteUserResponseAction, handleUsersResponse),
  on(deleteUsersResponseAction, handleUsersResponse),
  on(addUserResponseAction, handleUsersResponse),
);
