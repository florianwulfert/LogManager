import {createFeatureSelector, createSelector} from "@ngrx/store";
import {USER_FEATURE_NAME, UserState} from "./userState";
import {UserDto} from "../users/getUsers/user.dto";

const userGetState = createFeatureSelector<UserState>(USER_FEATURE_NAME);
export const getUser = createSelector(userGetState, (state: UserState): UserDto => state.response);
