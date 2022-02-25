import {createAction, props} from '@ngrx/store';
import {GetUserErrorResponse, GetUserResponse} from "./getUser/dto/get-user-response";
import {AddUserErrorResponse, AddUserResponse} from "./addUser/dto/add-user-response";
import {AddUserRequest} from "./addUser/dto/add-user-request";
import {DeleteUserErrorResponse, DeleteUserResponse} from "./deleteUser/dto/delete-user-response";

export const getUsersAction = createAction('Get users');
export const getUserResponseAction = createAction('Get list of users', props<GetUserResponse>());
export const loadGetUserErrorAction = createAction('Load Get users failure', props<GetUserErrorResponse>());

export const addUserAction = createAction('Add user', props<AddUserRequest>());
export const addUserResponseAction = createAction('Get response if user creation succeed', props<AddUserResponse>());
export const loadAddUserErrorAction = createAction('Load Add User failure', props<AddUserErrorResponse>());

export const deleteUserAction = createAction('Delete users');
export const deleteUserResponseAction = createAction('Get response if user deleting succeed', props<DeleteUserResponse>());
export const loadDeleteUserErrorAction = createAction('Load Delete User failure', props<DeleteUserErrorResponse>());
