import {createReducer, on} from "@ngrx/store";
import {GetUserResponse} from "./getUser/get-user-response";
import {USER_GET_INITIAL_STATE, UserState} from "./userState";
import {getUserResponseAction} from "./user.actions";

const handleUserResponse = (state: UserState, resp: GetUserResponse): UserState => {
  return {
    ...state,
    user: resp.user,
  };
};

export const UserReducer = createReducer(
  USER_GET_INITIAL_STATE,
  on(getUserResponseAction, handleUserResponse)
);
