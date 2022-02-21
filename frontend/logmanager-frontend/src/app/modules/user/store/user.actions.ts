import {createAction, props} from '@ngrx/store';
import {GetUserErrorResponse, GetUserResponse} from "../dto/get-user-response";

export const getUsersAction = createAction('Get users');
export const getUserResponseAction = createAction('Get list of users', props<GetUserResponse>());
export const loadGetUserErrorAction = createAction('Load Get users failure', props<GetUserErrorResponse>());
