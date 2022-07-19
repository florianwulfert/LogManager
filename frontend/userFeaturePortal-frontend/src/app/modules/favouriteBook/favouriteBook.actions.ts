import {createAction, props} from "@ngrx/store";
import {FavouriteBookErrorResponse, FavouriteBookResponse} from "./getFavouriteBook/favouriteBook-response";
import {AddBookRequest} from "../books/addBooks/add-book-request";
import {AddBookErrorResponse} from "../books/addBooks/add-book-response";

export const deleteFavouriteBookAction = createAction('Delete favourite book');
export const deleteFavouriteBookResponseAction = createAction('Get response if deleting the favourite book succeed', props<FavouriteBookResponse>());
export const loadDeleteFavouriteBookErrorAction = createAction('Load Delete favourite book failure', props<FavouriteBookErrorResponse>());

export const getFavouriteBookAction = createAction('Get favourite book request');
export const getFavouriteBookResponseAction = createAction('Get favourite book response', props<FavouriteBookResponse>());
export const loadGetFavouriteBookErrorAction = createAction('Load Get favourite book failure', props<FavouriteBookErrorResponse>());

export const assignBookToUserAction = createAction('Assign a book to user', props<AddBookRequest>());
export const assignBookToUserResponseAction = createAction('Get response if assigning of a book to an user succeed', props<FavouriteBookResponse>());
export const loadAssignBookToUserErrorAction = createAction('Load Assign book to user failure', props<AddBookErrorResponse>());
