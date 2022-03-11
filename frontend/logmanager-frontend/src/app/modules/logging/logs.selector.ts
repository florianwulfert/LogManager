import {createFeatureSelector, createSelector} from "@ngrx/store";
import {GET_LOGS_FEATURE_NAME, LogsGetState} from "./getLogs/store/logs-get.state";
import {LogsDto} from "./getLogs/dto/logs.dto";
import {DELETE_LOGS_FEATURE_NAME, LogsDeleteState} from "./deleteLogs/store/delete-logs.state";

const logsGetState = createFeatureSelector<LogsGetState>(GET_LOGS_FEATURE_NAME)
export const getLogs = createSelector(logsGetState, (state: LogsGetState): LogsDto[] => state.logsList)

const logsDeleteState = createFeatureSelector<LogsDeleteState>(DELETE_LOGS_FEATURE_NAME);
export const deleteLogs = createSelector(logsDeleteState, (state: LogsDeleteState): string => state.response);
