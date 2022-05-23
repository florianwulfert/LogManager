import {createAction, props} from "@ngrx/store";
import {FavouriteBookErrorResponse, FavouriteBookResponse} from "./getFavouriteBook/favouriteBook-response";

export const deleteFavouriteBookAction = createAction('Delete favourite book');
export const deleteFavouriteBookResponseAction = createAction('Get response if deleting the favourite book succeed', props<FavouriteBookResponse>());
export const loadDeleteFavouriteBookErrorAction = createAction('Load Delete favourite book failure', props<FavouriteBookErrorResponse>());

export const getFavouriteBookAction = createAction('Get favourite book request');
export const getFavouriteBookResponseAction = createAction('Get favourite book response', props<FavouriteBookResponse>());
export const loadGetFavouriteBookErrorAction = createAction('Load Get favourite book failure', props<FavouriteBookErrorResponse>());
