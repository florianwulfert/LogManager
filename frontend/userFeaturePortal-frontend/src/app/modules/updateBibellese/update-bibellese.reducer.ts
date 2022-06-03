import {createReducer, on} from "@ngrx/store";
import {updateBibelleseResponseAction} from "./update-bibellese.actions";
import {BIBELLESE_INITIAL_STATE, UpdateBibelleseState} from "./update-bibellese.state";
import {UpdateBibelleseResponse} from "./update-bibellese-response";

const handleBibelleseResponse = (state: UpdateBibelleseState, resp: UpdateBibelleseResponse): UpdateBibelleseState => {
  return {
    ...state,
    bibellese: resp.result,
  };
}

export const UpdateBibelleseReducer = createReducer(
  BIBELLESE_INITIAL_STATE,
  on(updateBibelleseResponseAction, handleBibelleseResponse)
)
