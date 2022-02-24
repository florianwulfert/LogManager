import {USER_INITIAL_STATE, USER_INTERACTION_INITIAL_STATE, UserInteraction, UserState} from "./user-state";
import {GetUserResponse} from "../dto/get-user-response";
import {createReducer, on} from "@ngrx/store";
import {addUserAction, addUserResponseAction, getUserResponseAction, getUsersAction} from "./user.actions";
import {AddUserResponse} from "../dto/add-user-response";

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

const handleAddUserResponse = (state: UserInteraction, resp: AddUserResponse): UserInteraction => {
  return {
    ...state,
    response: resp.result,
  };
}

const handleAddUser = (state: UserInteraction): UserInteraction => {
  return {
    ...state,
  };
};

export const UserReducer = createReducer(
  USER_INITIAL_STATE,
  on(getUserResponseAction, handleGetUserResponse),
  on(getUsersAction, handleGetUser)
);

export const UserInteractionReducer = createReducer(
  USER_INTERACTION_INITIAL_STATE,
  on(addUserResponseAction, handleAddUserResponse),
  on(addUserAction, handleAddUser)
)
