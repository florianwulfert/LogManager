import {createReducer, on} from "@ngrx/store";
import {AddLogResponse} from "../dto/add-log-response";
import {LOG_ADD_INITIAL_STATE, LogAddState} from "./log-add.state";
import {addLogAction, addLogResponseAction} from "../../logs.actions";

const handleAddLogResponse = (state: LogAddState, resp: AddLogResponse): LogAddState => {
  return {
    ...state,
    response: resp.result,
  };
}

const handleAddLog = (state: LogAddState): LogAddState => {
  return {
    ...state,
  };
};

export const LogAddReducer = createReducer(
  LOG_ADD_INITIAL_STATE,
  on(addLogResponseAction, handleAddLogResponse),
  on(addLogAction, handleAddLog)
)
