import {createAction, props} from "@ngrx/store";
import {GetLogsErrorResponse, GetLogsResponse} from "./getLogs/dto/get-logs-response";

export const getLogsAction = createAction('Get logs');
export const getLogsResponseAction = createAction('Get list of logs', props<GetLogsResponse>())
export const loadGetLogsErrorAction = createAction('Loag Get logs failure', props<GetLogsErrorResponse>())

