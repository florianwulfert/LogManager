import {createReducer, on} from "@ngrx/store";
import {
  addBibelleseResponseAction, deleteBibelleseResponseAction,
  getBibelleseResponseAction
} from "./bibellese.actions";
import {BIBELLESE_INITIAL_STATE, BibelleseState} from "./bibellese.state";
import {GetBibelleseResponse} from "./getBibellese/get-bibellese-response";

const handleBooksResponse = (state: BibelleseState, resp: GetBibelleseResponse): BibelleseState => {
  return {
    ...state,
    bibelleseList: resp.result,
  };
}

export const BibelleseReducer = createReducer(
  BIBELLESE_INITIAL_STATE,
  on(getBibelleseResponseAction, handleBooksResponse),
  on(addBibelleseResponseAction, handleBooksResponse),
  on(deleteBibelleseResponseAction, handleBooksResponse),
)
