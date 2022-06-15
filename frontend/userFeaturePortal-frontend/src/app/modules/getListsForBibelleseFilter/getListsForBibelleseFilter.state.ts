import {GetListsDto} from "./getLists.dto";

export const GET_LISTS_FOR_FILTER_BIBELLESE_FEATURE_NAME = 'getListsForBibelleseFilter'

export interface GetListsForBibelleseFilterState {
  listsForFilterBibellese: GetListsDto[]
}

export const GET_LISTS_FOR_FILTER_BIBELLESE_INITIAL_STATE: GetListsForBibelleseFilterState = {
  listsForFilterBibellese: [],
}
