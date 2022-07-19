import {createAction, props} from '@ngrx/store';
import {GetBookRequest} from "./getBook/get-book-request";
import {GetBookErrorResponse, GetBookResponse} from "./getBook/get-book-response";

export const getBookAction = createAction('Get book request', props<GetBookRequest>());
export const getBookResponseAction = createAction('Get book response', props<GetBookResponse>());
export const loadGetBookErrorAction = createAction('Load Get book failure', props<GetBookErrorResponse>());
