import {createReducer, on} from "@ngrx/store";
import {
  assignBookToUserResponseAction,
  deleteFavouriteBookResponseAction,
  getFavouriteBookResponseAction
} from "./favouriteBook.actions";
import {FAVOURITE_BOOK_GET_INITIAL_STATE, FavouriteBookState} from "./favouriteBook.state";
import {FavouriteBookResponse} from "./getFavouriteBook/favouriteBook-response";

const handleBooksResponse = (state: FavouriteBookState, resp: FavouriteBookResponse): FavouriteBookState => {
  return {
    ...state,
    favouriteBook: resp.favouriteBook,
  };
}

export const FavouriteBookReducer = createReducer(
  FAVOURITE_BOOK_GET_INITIAL_STATE,
  on(deleteFavouriteBookResponseAction, handleBooksResponse),
  on(getFavouriteBookResponseAction, handleBooksResponse),
  on(assignBookToUserResponseAction, handleBooksResponse)
)
