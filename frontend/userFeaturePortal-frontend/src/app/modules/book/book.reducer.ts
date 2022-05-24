import {createReducer, on} from "@ngrx/store";
import {BOOK_GET_INITIAL_STATE, BookState} from "./book.state";
import {getBookResponseAction} from "./book.actions";
import {GetBookResponse} from "./getBook/get-book-response";

const handleBookResponse = (state: BookState, resp: GetBookResponse): BookState => {
  return {
    ...state,
    book: resp.book,
  };
};

export const BookReducer = createReducer(
  BOOK_GET_INITIAL_STATE,
  on(getBookResponseAction, handleBookResponse)
);
