import {createFeatureSelector, createSelector} from "@ngrx/store";
import {USER_FEATURE_NAME, UserInteraction, UserState} from "./user-state";
import {UserDto} from "../dto/user.dto";

const userState = createFeatureSelector<UserState>(USER_FEATURE_NAME);
export const getUsers = createSelector(userState, (state: UserState): UserDto[] => state.userList);
export const getUsersError = createSelector(userState, (state: UserState): string => state.error);

const userInteraction = createFeatureSelector<UserInteraction>(USER_FEATURE_NAME);
export const addUser = createSelector(userInteraction, (state: UserInteraction): string => state.response);
export const addUserError = createSelector(userInteraction, (state: UserInteraction): string => state.error);

