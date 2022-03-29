import {createReducer, on} from "@ngrx/store";
import {DeleteLogsResponse} from "./deleteLogs/dto/delete-logs-response";
import {deleteLogsAction, deleteLogsResponseAction} from "./logs.actions";
import {LOGS_GET_INITIAL_STATE, LogsState} from "./logs.state";

const handleDeleteLogsResponse = (state: LogsState, resp: DeleteLogsResponse): LogsState => {
  return {
    ...state,
    logsList: resp.logsList,
  };
}

const handleDeleteLogs = (state: LogsState): LogsState => {
  return {
    ...state,
  };
};

export const LogsReducer = createReducer(
  LOGS_GET_INITIAL_STATE,
  on(deleteLogsResponseAction, handleDeleteLogsResponse),
  on(deleteLogsAction, handleDeleteLogs)
)
