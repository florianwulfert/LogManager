import {createFeatureSelector, createSelector} from "@ngrx/store";
import {UserDto} from "./getUser/dto/user.dto";
import {DELETE_USER_FEATURE_NAME, UserDeleteState} from "./deleteUser/store/user-delete.state";
import {ADD_USER_FEATURE_NAME, UserAddState} from "./addUser/store/user-add.state";
import {GET_USER_FEATURE_NAME, UserGetState} from "./getUser/store/user-get.state";
import {DELETE_USERS_FEATURE_NAME, UsersDeleteState} from "./deleteUsers/store/user-delete.state";

const userGetState = createFeatureSelector<UserGetState>(GET_USER_FEATURE_NAME);
export const getUsers = createSelector(userGetState, (state: UserGetState): UserDto[] => state.userList);

const userAddState = createFeatureSelector<UserAddState>(ADD_USER_FEATURE_NAME);
export const addUser = createSelector(userAddState, (state: UserAddState): string => state.response);

const usersDeleteState = createFeatureSelector<UsersDeleteState>(DELETE_USERS_FEATURE_NAME);
export const deleteUsers = createSelector(usersDeleteState, (state: UsersDeleteState): string => state.response);

const userDeleteState = createFeatureSelector<UserDeleteState>(DELETE_USER_FEATURE_NAME);
export const deleteUser = createSelector(userDeleteState, (state: UserDeleteState): string => state.response);
