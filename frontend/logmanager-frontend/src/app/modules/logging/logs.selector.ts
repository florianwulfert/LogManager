import {createFeatureSelector, createSelector} from "@ngrx/store";
import {GET_LOGS_FEATURE_NAME, LogsGetState} from "./getLogs/store/logs-get.state";
import {LogsDto} from "./getLogs/dto/logs.dto";
import {DELETE_LOGS_FEATURE_NAME, LogsDeleteState} from "./deleteLogs/store/delete-logs.state";
import {ADD_LOG_FEATURE_NAME, LogAddState} from "./addLogs/store/log-add.state";
import {DELETE_LOG_FEATURE_NAME, LogDeleteState} from "./deleteLog/store/log-delete.state";

const logsGetState = createFeatureSelector<LogsGetState>(GET_LOGS_FEATURE_NAME)
export const getLogs = createSelector(logsGetState, (state: LogsGetState): LogsDto[] => state.logsList)

const logsDeleteState = createFeatureSelector<LogsDeleteState>(DELETE_LOGS_FEATURE_NAME);
export const deleteLogs = createSelector(logsDeleteState, (state: LogsDeleteState): string => state.response);

const logAddState = createFeatureSelector<LogAddState>(ADD_LOG_FEATURE_NAME);
export const addLog = createSelector(logAddState, (state: LogAddState): string => state.response);

const logDeleteState = createFeatureSelector<LogDeleteState>(DELETE_LOG_FEATURE_NAME);
export const deleteLog = createSelector(logDeleteState, (state: LogDeleteState): string => state.response);
