import {createReducer, on} from "@ngrx/store";
import {addBookResponseAction, deleteBookResponseAction, getBooksResponseAction} from "./books.actions";
import {BOOKS_GET_INITIAL_STATE, BooksState} from "./books.state";
import {GetBooksResponse} from "./getBooks/get-books-response";

const handleBooksResponse = (state: BooksState, resp: GetBooksResponse): BooksState => {
  return {
    ...state,
    booksList: resp.result,
  };
}

export const BooksReducer = createReducer(
  BOOKS_GET_INITIAL_STATE,
  on(getBooksResponseAction, handleBooksResponse),
  on(addBookResponseAction, handleBooksResponse),
  on(deleteBookResponseAction, handleBooksResponse)
)
