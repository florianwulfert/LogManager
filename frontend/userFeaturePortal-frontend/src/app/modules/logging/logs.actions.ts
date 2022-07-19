import {createAction, props} from "@ngrx/store";
import {GetLogsErrorResponse, GetLogsResponse} from "./getLogs/dto/get-logs-response";
import {DeleteLogsErrorResponse, DeleteLogsResponse} from "./deleteLogs/dto/delete-logs-response";
import {AddLogErrorResponse, AddLogResponse} from "./addLogs/dto/add-log-response";
import {DeleteLogErrorResponse, DeleteLogResponse} from "./deleteLog/dto/delete-log-response";
import {GetLogsRequest} from "./getLogs/dto/getLogs-request";
import {AddLogRequestWithFilter} from "./addLogs/dto/add-log-request-with-filter";

export const getLogsAction = createAction('Get logs', props<GetLogsRequest>());
export const getLogsResponseAction = createAction('Get list of logs', props<GetLogsResponse>())
export const loadGetLogsErrorAction = createAction('Load Get logs failure', props<GetLogsErrorResponse>())

export const deleteLogsAction = createAction('Delete logs');
export const deleteLogsResponseAction = createAction('Get response if logs deleting succeed', props<DeleteLogsResponse>());
export const loadDeleteLogsErrorAction = createAction('Load Delete Logs failure', props<DeleteLogsErrorResponse>());

export const addLogAction = createAction('Add log', props<AddLogRequestWithFilter>());
export const addLogResponseAction = createAction('Get response if log creation succeed', props<AddLogResponse>());
export const loadAddLogErrorAction = createAction('Load Add Log failure', props<AddLogErrorResponse>());

export const deleteLogAction = createAction('Delete one log', props<GetLogsRequest>());
export const deleteLogResponseAction = createAction('Get response if deleting of one log succeed', props<DeleteLogResponse>());
export const loadDeleteLogErrorAction = createAction('Load Delete Log failure', props<DeleteLogErrorResponse>());
