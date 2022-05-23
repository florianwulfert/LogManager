import {createFeatureSelector, createSelector} from "@ngrx/store";
import {FAVOURITE_BOOK_FEATURE_NAME, FavouriteBookState} from "./favouriteBook.state";

const favouriteBookGetState = createFeatureSelector<FavouriteBookState>(FAVOURITE_BOOK_FEATURE_NAME)
export const getFavouriteBook = createSelector(favouriteBookGetState, (state: FavouriteBookState): string => state.favouriteBook)
