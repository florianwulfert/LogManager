import {AddUserResponse} from "../dto/add-user-response";
import {createReducer, on} from "@ngrx/store";
import {USER_ADD_INITIAL_STATE, UserAddState} from "./user-add.state";
import {addUserAction, addUserResponseAction} from "../../user.actions";

const handleAddUserResponse = (state: UserAddState, resp: AddUserResponse): UserAddState => {
  return {
    ...state,
    response: resp.result,
  };
}

const handleAddUser = (state: UserAddState): UserAddState => {
  return {
    ...state,
  };
};

export const UserAddReducer = createReducer(
  USER_ADD_INITIAL_STATE,
  on(addUserResponseAction, handleAddUserResponse),
  on(addUserAction, handleAddUser)
)
