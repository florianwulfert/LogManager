import {USER_INITIAL_STATE, UserState} from "./user-state";
import {GetUserResponse} from "../dto/get-user-response";
import {createReducer, on} from "@ngrx/store";
import {getUsersAction} from "./user.actions";

const handleGetUserResponse = (state: UserState, resp: GetUserResponse): UserState => {
  return {
    ...state,
    userList: resp.result
  };
};

export const UserReducer = createReducer(USER_INITIAL_STATE, on(getUsersAction, handleGetUserResponse));
