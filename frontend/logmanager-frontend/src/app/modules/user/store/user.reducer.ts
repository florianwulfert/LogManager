import {USER_INITIAL_STATE, UserState} from "./user-state";
import {GetUserResponse} from "../dto/get-user-response";
import {createReducer, on} from "@ngrx/store";
import {getUserResponseAction, getUsersAction} from "./user.actions";

const handleGetUserResponse = (state: UserState, resp: GetUserResponse): UserState => {
  return {
    ...state,
    userList: resp.result,
  };
};

const handleGetUser = (state: UserState): UserState => {
  return {
    ...state,
  };
};

export const UserReducer = createReducer(
  USER_INITIAL_STATE,
  on(getUserResponseAction, handleGetUserResponse),
  on(getUsersAction, handleGetUser)
);
