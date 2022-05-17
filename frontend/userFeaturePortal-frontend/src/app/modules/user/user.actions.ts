import {createAction, props} from '@ngrx/store';
import {GetUserErrorResponse, GetUserResponse} from "./getUser/get-user-response";

export const getUserAction = createAction('Get user request');
export const getUserResponseAction = createAction('Get user response', props<GetUserResponse>());
export const loadGetUserErrorAction = createAction('Load Get user failure', props<GetUserErrorResponse>());
