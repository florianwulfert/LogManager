import {createFeatureSelector, createSelector} from "@ngrx/store";
import {LOGS_FEATURE_NAME, LogsState} from "./logs.state";
import {LogsDto} from "./getLogs/dto/logs.dto";

const logsGetState = createFeatureSelector<LogsState>(LOGS_FEATURE_NAME)
export const getLogs = createSelector(logsGetState, (state: LogsState): LogsDto[] => state.logsList)
