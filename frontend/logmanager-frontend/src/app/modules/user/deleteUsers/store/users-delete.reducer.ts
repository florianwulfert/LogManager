import {createReducer, on} from "@ngrx/store";
import {USERS_DELETE_INITIAL_STATE, UsersDeleteState} from "./user-delete.state";
import {DeleteUsersResponse} from "../dto/delete-users-response";
import {deleteUsersAction, deleteUsersResponseAction} from "../../user.actions";

const handleDeleteUsersResponse = (state: UsersDeleteState, resp: DeleteUsersResponse): UsersDeleteState => {
  return {
    ...state,
    response: resp.result,
  };
}

const handleDeleteUsers = (state: UsersDeleteState): UsersDeleteState => {
  return {
    ...state,
  };
};

export const UsersDeleteReducer = createReducer(
  USERS_DELETE_INITIAL_STATE,
  on(deleteUsersResponseAction, handleDeleteUsersResponse),
  on(deleteUsersAction, handleDeleteUsers)
)
