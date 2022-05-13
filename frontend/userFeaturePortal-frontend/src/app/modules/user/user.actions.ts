import {createAction, props} from '@ngrx/store';
import {GetUsersErrorResponse, GetUsersResponse} from "./getUsers/get-users-response";
import {AddUserErrorResponse, AddUserResponse} from "./addUser/add-user-response";
import {AddUserRequest} from "./addUser/add-user-request";
import {DeleteUserErrorResponse, DeleteUserResponse} from "./deleteUser/delete-user-response";
import {DeleteUsersErrorResponse, DeleteUsersResponse} from "./deleteUsers/delete-users-response";
import {DeleteUserRequest} from "./deleteUser/delete-user-request";
import {GetUserErrorResponse, GetUserResponse} from "./getUser/get-user-response";

export const getUsersAction = createAction('Get users');
export const getUsersResponseAction = createAction('Get list of users', props<GetUsersResponse>());
export const loadGetUsersErrorAction = createAction('Load Get users failure', props<GetUsersErrorResponse>());

export const getUserAction = createAction('Get user');
export const getUserResponseAction = createAction('Get user', props<GetUserResponse>());
export const loadGetUserErrorAction = createAction('Load Get user failure', props<GetUserErrorResponse>());

export const addUserAction = createAction('Add user', props<AddUserRequest>());
export const addUserResponseAction = createAction('Get response if user creation succeed', props<AddUserResponse>());
export const loadAddUserErrorAction = createAction('Load Add User failure', props<AddUserErrorResponse>());

export const deleteUserAction = createAction('Delete one user', props<DeleteUserRequest>());
export const deleteUserResponseAction = createAction('Get response if deleting of one user succeed', props<DeleteUserResponse>());
export const loadDeleteUserErrorAction = createAction('Load Delete User failure', props<DeleteUserErrorResponse>());

export const deleteUsersAction = createAction('Delete users');
export const deleteUsersResponseAction = createAction('Get response if user deleting succeed', props<DeleteUsersResponse>());
export const loadDeleteUsersErrorAction = createAction('Load Delete Users failure', props<DeleteUsersErrorResponse>());
