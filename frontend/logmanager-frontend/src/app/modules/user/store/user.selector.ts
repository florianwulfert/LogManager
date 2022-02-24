import {createFeatureSelector, createSelector} from "@ngrx/store";
import {USER_FEATURE_NAME, UserState} from "./user-state";
import {UserDto} from "../dto/user.dto";

const userState = createFeatureSelector<UserState>(USER_FEATURE_NAME);
export const getUsers = createSelector(userState, (state: UserState): UserDto[] => state.userList);
export const getUsersError = createSelector(userState, (state: UserState): string => state.error);

export const addUser = createSelector(userState, (state: UserState): UserDto[] => state.userList);
export const addUserError = createSelector(userState, (state: UserState): string => state.error);

