import {createAction, props} from "@ngrx/store";
import {
  GetListsForFilterBibelleseErrorResponse,
  GetListsForFilterBibelleseResponse
} from "./getListsForFilterBibellese-response";

export const getAllBibelleseAction = createAction('Get Lists for Bibellese filter');
export const getAllBibelleseResponseAction = createAction('Get list of lists for Bibelleses filter', props<GetListsForFilterBibelleseResponse>())
export const loadGetAllBibelleseErrorAction = createAction('Load Get lists for Bibelleses filter failure', props<GetListsForFilterBibelleseErrorResponse>())
