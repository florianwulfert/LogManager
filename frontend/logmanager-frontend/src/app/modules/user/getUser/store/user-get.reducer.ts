import {createReducer, on} from "@ngrx/store";
import {USER_GET_INITIAL_STATE, UserGetState} from "./user-get.state";
import {getUserResponseAction, getUsersAction} from "../../user.actions";
import {GetUserResponse} from "../dto/get-user-response";

const handleGetUserResponse = (state: UserGetState, resp: GetUserResponse): UserGetState => {
  return {
    ...state,
    userList: resp.result,
  };
};

const handleGetUser = (state: UserGetState): UserGetState => {
  return {
    ...state,
  };
};

export const UserGetReducer = createReducer(
  USER_GET_INITIAL_STATE,
  on(getUserResponseAction, handleGetUserResponse),
  on(getUsersAction, handleGetUser)
);
