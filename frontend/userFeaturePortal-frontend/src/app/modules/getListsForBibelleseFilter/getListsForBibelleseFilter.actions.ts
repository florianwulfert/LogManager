import {createAction, props} from "@ngrx/store";
import {
  GetListsForFilterBibelleseErrorResponse,
  GetListsForFilterBibelleseResponse
} from "./getListsForFilterBibellese-response";

export const getAllBibelleseAction = createAction('Get All Bibelleses');
export const getAllBibelleseResponseAction = createAction('Get list of All Bibelleses', props<GetListsForFilterBibelleseResponse>())
export const loadGetAllBibelleseErrorAction = createAction('Load Get All Bibelleses failure', props<GetListsForFilterBibelleseErrorResponse>())
