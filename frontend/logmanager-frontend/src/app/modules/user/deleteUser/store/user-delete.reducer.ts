import {createReducer, on} from "@ngrx/store";
import {deleteUserAction, deleteUserResponseAction} from "../../user.actions";
import {USER_DELETE_INITIAL_STATE, UserDeleteState} from "./user-delete.state";
import {DeleteUserResponse} from "../dto/delete-user-response";

const handleDeleteUserResponse = (state: UserDeleteState, resp: DeleteUserResponse): UserDeleteState => {
  return {
    ...state,
    response: resp.result,
  };
}

const handleDeleteUser = (state: UserDeleteState): UserDeleteState => {
  return {
    ...state,
  };
};

export const UserDeleteReducer = createReducer(
  USER_DELETE_INITIAL_STATE,
  on(deleteUserResponseAction, handleDeleteUserResponse),
  on(deleteUserAction, handleDeleteUser)
)
