import {createReducer, on} from "@ngrx/store";
import {getAllBibelleseResponseAction} from "./getListsForBibelleseFilter.actions";
import {
  GET_LISTS_FOR_FILTER_BIBELLESE_INITIAL_STATE,
  GetListsForBibelleseFilterState
} from "./getListsForBibelleseFilter.state";
import {GetListsForFilterBibelleseResponse} from "./getListsForFilterBibellese-response";

const handleGetListsResponse = (state: GetListsForBibelleseFilterState, resp: GetListsForFilterBibelleseResponse): GetListsForBibelleseFilterState => {
  console.log(resp)
  return {
    ...state,
    listsForFilterBibellese: resp.result,
  };
}

export const GetListsForBibelleseFilterReducer = createReducer(
  GET_LISTS_FOR_FILTER_BIBELLESE_INITIAL_STATE,
  on(getAllBibelleseResponseAction, handleGetListsResponse)
)
