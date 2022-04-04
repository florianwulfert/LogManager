import {createReducer, on} from "@ngrx/store";
import {LOG_DELETE_INITIAL_STATE, LogDeleteState} from "./log-delete.state";
import {deleteLogAction, deleteLogResponseAction} from "../../logs.actions";
import {DeleteLogResponse} from "../dto/delete-log-response";

const handleDeleteLogResponse = (state: LogDeleteState, resp: DeleteLogResponse): LogDeleteState => {
  return {
    ...state,
    response: resp.result,
  };
}

const handleDeleteLog = (state: LogDeleteState): LogDeleteState => {
  return {
    ...state,
  };
};

export const LogDeleteReducer = createReducer(
  LOG_DELETE_INITIAL_STATE,
  on(deleteLogResponseAction, handleDeleteLogResponse),
  on(deleteLogAction, handleDeleteLog)
)
