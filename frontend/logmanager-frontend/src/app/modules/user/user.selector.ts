import {createFeatureSelector, createSelector} from "@ngrx/store";
import {UserDto} from "./getUser/dto/user.dto";
import {ADD_USER_FEATURE_NAME, UserAddState} from "./addUser/store/user-add.state";
import {GET_USER_FEATURE_NAME, UserGetState} from "./getUser/store/user-get.state";

const userGetState = createFeatureSelector<UserGetState>(GET_USER_FEATURE_NAME);
export const getUsers = createSelector(userGetState, (state: UserGetState): UserDto[] => state.userList);

const userAddState = createFeatureSelector<UserAddState>(ADD_USER_FEATURE_NAME);
export const addUser = createSelector(userAddState, (state: UserAddState): string => state.response);
