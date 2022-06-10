import {createAction, props} from "@ngrx/store";
import {UpdateBibelleseErrorResponse, UpdateBibelleseResponse} from "./update-bibellese-response";
import {UpdateBibelleseRequest} from "./update-bibellese-request";

export const updateBibelleseAction = createAction('Update Bibellese', props<UpdateBibelleseRequest>());
export const updateBibelleseResponseAction = createAction('Get response if Bibellese updating succeed', props<UpdateBibelleseResponse>());
export const loadUpdateBibelleseErrorAction = createAction('Load Update Bibellese failure', props<UpdateBibelleseErrorResponse>());
