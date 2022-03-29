import {createReducer, on} from "@ngrx/store";
import {LOGS_GET_INITIAL_STATE, LogsState} from "../../logs.state";
import {getLogsAction, getLogsResponseAction} from "../../logs.actions";
import {GetLogsResponse} from "../dto/get-logs-response";

const handleGetLogsResponse = (state: LogsState, resp: GetLogsResponse): LogsState => {
  return {
    ...state,
    logsList: resp.result,
  };
};

const handleGetLogs = (state: LogsState): LogsState => {
  return {
    ...state,
  };
};

export const LogsGetReducer = createReducer(
  LOGS_GET_INITIAL_STATE,
  on(getLogsResponseAction, handleGetLogsResponse),
  on(getLogsAction, handleGetLogs)
);
