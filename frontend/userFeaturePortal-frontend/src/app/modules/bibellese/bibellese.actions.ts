import {createAction, props} from "@ngrx/store";
import {GetBibelleseErrorResponse, GetBibelleseResponse} from "./getBibellese/get-bibellese-response";
import {GetBibelleseRequest} from "./getBibellese/get-bibellese-request";
import {AddBibelleseRequest} from "./addBibellese/add-bibellese-request";
import {AddBibelleseErrorResponse, AddBibelleseResponse} from "./addBibellese/add-bibellese-response";
import {DeleteBibelleseRequest} from "./deleteBibellese/delete-bibellese-request";
import {DeleteBibelleseErrorResponse, DeleteBibelleseResponse} from "./deleteBibellese/delete-bibellese-response";

export const getBibelleseAction = createAction('Get Bibelleses', props<GetBibelleseRequest>());
export const getBibelleseResponseAction = createAction('Get list of Bibelleses', props<GetBibelleseResponse>())
export const loadGetBibelleseErrorAction = createAction('Load Get Bibelleses failure', props<GetBibelleseErrorResponse>())

export const addBibelleseAction = createAction('Add Bibellese', props<AddBibelleseRequest>());
export const addBibelleseResponseAction = createAction('Get response if Bibellese addition succeed', props<AddBibelleseResponse>());
export const loadAddBibelleseErrorAction = createAction('Load Add Bibellese failure', props<AddBibelleseErrorResponse>());

export const deleteBibelleseAction = createAction('Delete one Bibellese', props<DeleteBibelleseRequest>());
export const deleteBibelleseResponseAction = createAction('Get response if deleting of one Bibellese succeed', props<DeleteBibelleseResponse>());
export const loadDeleteBibelleseErrorAction = createAction('Load Delete Bibellese failure', props<DeleteBibelleseErrorResponse>());
