import {createFeatureSelector, createSelector} from "@ngrx/store";
import {UserDto} from "./getUsers/user.dto";
import {USERS_FEATURE_NAME, UsersState} from "./usersState";

const userGetState = createFeatureSelector<UsersState>(USERS_FEATURE_NAME);
export const getUsers = createSelector(userGetState, (state: UsersState): UserDto[] => state.userList);
