import {createAction, props} from "@ngrx/store";
import {GetLogsErrorResponse, GetLogsResponse} from "./getLogs/dto/get-logs-response";
import {DeleteLogsErrorResponse, DeleteLogsResponse} from "./deleteLogs/dto/delete-logs-response";

export const getLogsAction = createAction('Get logs');
export const getLogsResponseAction = createAction('Get list of logs', props<GetLogsResponse>())
export const loadGetLogsErrorAction = createAction('Load Get logs failure', props<GetLogsErrorResponse>())

export const deleteLogsAction = createAction('Delete logs');
export const deleteLogsResponseAction = createAction('Get response if logs deleting succeed', props<DeleteLogsResponse>());
export const loadDeleteLogsErrorAction = createAction('Load Delete Logs failure', props<DeleteLogsErrorResponse>());
