import {createReducer, on} from "@ngrx/store";
import {
  addBookResponseAction,
  assignBookToUserResponseAction,
  deleteBookResponseAction,
  getBooksResponseAction
} from "./bibellese.actions";
import {BOOKS_GET_INITIAL_STATE, BibelleseState} from "./bibellese.state";
import {GetBibelleseResponse} from "./getBibellese/get-bibellese-response";

const handleBooksResponse = (state: BibelleseState, resp: GetBibelleseResponse): BibelleseState => {
  return {
    ...state,
    booksList: resp.result,
  };
}

export const BibelleseReducer = createReducer(
  BOOKS_GET_INITIAL_STATE,
  on(getBooksResponseAction, handleBooksResponse),
  on(addBookResponseAction, handleBooksResponse),
  on(deleteBookResponseAction, handleBooksResponse),
  on(assignBookToUserResponseAction, handleBooksResponse)
)
