import {createReducer, on} from "@ngrx/store";
import {DeleteLogsResponse} from "../dto/delete-logs-response";
import {LOGS_DELETE_INITIAL_STATE, LogsDeleteState} from "./delete-logs.state";
import {deleteLogsAction, deleteLogsResponseAction} from "../../logs.actions";

const handleDeleteLogsResponse = (state: LogsDeleteState, resp: DeleteLogsResponse): LogsDeleteState => {
  return {
    ...state,
    response: resp.result,
  };
}

const handleDeleteLogs = (state: LogsDeleteState): LogsDeleteState => {
  return {
    ...state,
  };
};

export const LogsDeleteReducer = createReducer(
  LOGS_DELETE_INITIAL_STATE,
  on(deleteLogsResponseAction, handleDeleteLogsResponse),
  on(deleteLogsAction, handleDeleteLogs)
)
