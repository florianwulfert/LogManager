import {createReducer, on} from "@ngrx/store";
import {addLogResponseAction, deleteLogsResponseAction, getLogsResponseAction} from "./logs.actions";
import {LOGS_GET_INITIAL_STATE, LogsState} from "./logs.state";
import {GetLogsResponse} from "./getLogs/dto/get-logs-response";

const handleLogsResponse = (state: LogsState, resp: GetLogsResponse): LogsState => {
  return {
    ...state,
    logsList: resp.logsList,
  };
}

export const LogsReducer = createReducer(
  LOGS_GET_INITIAL_STATE,
  on(deleteLogsResponseAction, handleLogsResponse),
  on(getLogsResponseAction, handleLogsResponse),
  on(addLogResponseAction, handleLogsResponse)
)
