import {createReducer, on} from "@ngrx/store";
import {LOGS_GET_INITIAL_STATE, LogsGetState} from "./logs-get.state";
import {getLogsAction, getLogsResponseAction} from "../../logs.actions";
import {GetLogsResponse} from "../dto/get-logs-response";

const handleGetLogsResponse = (state: LogsGetState, resp: GetLogsResponse): LogsGetState => {
  return {
    ...state,
    logsList: resp.result,
  };
};

const handleGetLogs = (state: LogsGetState): LogsGetState => {
  return {
    ...state,
  };
};

export const LogsGetReducer = createReducer(
  LOGS_GET_INITIAL_STATE,
  on(getLogsResponseAction, handleGetLogsResponse),
  on(getLogsAction, handleGetLogs)
);
