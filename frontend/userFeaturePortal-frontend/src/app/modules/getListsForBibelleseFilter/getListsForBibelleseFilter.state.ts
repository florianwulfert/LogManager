import {GetListsDto} from "./getLists.dto";

export const GET_LISTS_FOR_FILTER_BIBELLESE_FEATURE_NAME = 'get lists'

export interface GetListsForBibelleseFilterState {
  result: GetListsDto[]
}

const getListsDto: GetListsDto[] = [{
  bibelabschnitte: [],
  labels: [],
  lieblingsverse: []
}]

export const GET_LISTS_FOR_FILTER_BIBELLESE_INITIAL_STATE: GetListsForBibelleseFilterState = {
  result: getListsDto,
}
