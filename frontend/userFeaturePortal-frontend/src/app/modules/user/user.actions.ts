import {createAction, props} from '@ngrx/store';
import {GetUserErrorResponse, GetUserResponse} from "./getUser/get-user-response";
import {GetUserRequest} from "./getUser/getUser-request";

export const getUserAction = createAction('Get user request', props<GetUserRequest>());
export const getUserResponseAction = createAction('Get user response', props<GetUserResponse>());
export const loadGetUserErrorAction = createAction('Load Get user failure', props<GetUserErrorResponse>());
