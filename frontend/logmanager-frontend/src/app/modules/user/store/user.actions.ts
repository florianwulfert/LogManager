import {createAction, props} from '@ngrx/store';
import {GetUserErrorResponse, GetUserResponse} from "../dto/get-user-response";
import {AddUserErrorResponse, AddUserResponse} from "../dto/add-user-response";
import {AddUserRequest} from "../dto/add-user-request";

export const getUsersAction = createAction('Get users');
export const getUserResponseAction = createAction('Get list of users', props<GetUserResponse>());
export const loadGetUserErrorAction = createAction('Load Get users failure', props<GetUserErrorResponse>());

export const addUserAction = createAction('Add user', props<AddUserRequest>());
export const addUserResponseAction = createAction('Get response if user creation succeed', props<AddUserResponse>());
export const loadAddUserErrorAction = createAction('Load Add User failure', props<AddUserErrorResponse>());
