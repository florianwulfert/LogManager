import {createFeatureSelector, createSelector} from "@ngrx/store";
import {UserDto} from "./getUsers/user.dto";
import {USER_FEATURE_NAME, UserState} from "./user.state";

const userGetState = createFeatureSelector<UserState>(USER_FEATURE_NAME);
export const getUsers = createSelector(userGetState, (state: UserState): UserDto[] => state.userList);
