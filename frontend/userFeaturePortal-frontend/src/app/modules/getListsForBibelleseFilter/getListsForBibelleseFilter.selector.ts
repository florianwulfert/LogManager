import {createFeatureSelector, createSelector} from "@ngrx/store";
import {GetListsDto} from "./getLists.dto";
import {
  GET_LISTS_FOR_FILTER_BIBELLESE_FEATURE_NAME,
  GetListsForBibelleseFilterState
} from "./getListsForBibelleseFilter.state";

const bibelleseState = createFeatureSelector<GetListsForBibelleseFilterState>(GET_LISTS_FOR_FILTER_BIBELLESE_FEATURE_NAME)
export const getLists = createSelector(bibelleseState, (state: GetListsForBibelleseFilterState): GetListsDto[] => state.listsForFilterBibellese)
