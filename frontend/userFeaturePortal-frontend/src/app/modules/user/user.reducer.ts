import {createReducer, on} from "@ngrx/store";
import {USER_GET_INITIAL_STATE, UserState} from "./user.state";
import {
  addUserResponseAction,
  deleteUserResponseAction,
  deleteUsersResponseAction,
  getUserResponseAction
} from "./user.actions";
import {GetUserResponse} from "./getUser/get-user-response";

const handleUserResponse = (state: UserState, resp: GetUserResponse): UserState => {
  return {
    ...state,
    userList: resp.result,
  };
};

export const UserReducer = createReducer(
  USER_GET_INITIAL_STATE,
  on(getUserResponseAction, handleUserResponse),
  on(deleteUserResponseAction, handleUserResponse),
  on(deleteUsersResponseAction, handleUserResponse),
  on(addUserResponseAction, handleUserResponse),
);
