import {createAction, props} from "@ngrx/store";
import {GetBooksErrorResponse, GetBooksResponse} from "./getBooks/get-books-response";

export const getBooksAction = createAction('Get books');
export const getBooksResponseAction = createAction('Get list of books', props<GetBooksResponse>())
export const loadGetBooksErrorAction = createAction('Load Get books failure', props<GetBooksErrorResponse>())
