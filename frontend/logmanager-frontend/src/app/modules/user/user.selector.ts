import {createFeatureSelector, createSelector} from "@ngrx/store";
import {UserDto} from "./getUser/dto/user.dto";
import {USER_FEATURE_NAME, UserState} from "./getUser/store/user.state";

const userGetState = createFeatureSelector<UserState>(USER_FEATURE_NAME);
export const getUsers = createSelector(userGetState, (state: UserState): UserDto[] => state.userList);
