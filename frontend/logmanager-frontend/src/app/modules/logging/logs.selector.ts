import {createFeatureSelector, createSelector} from "@ngrx/store";
import {GET_LOGS_FEATURE_NAME, LogsGetState} from "./getLogs/store/logs-get.state";
import {LogsDto} from "./getLogs/dto/logs.dto";

const logsGetState = createFeatureSelector<LogsGetState>(GET_LOGS_FEATURE_NAME)
export const getLogs = createSelector(logsGetState, (state: LogsGetState): LogsDto[] => state.logsList)
