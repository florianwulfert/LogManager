import {Injectable} from "@angular/core";
import {Store} from "@ngrx/store";
import {addLogAction, deleteLogAction, deleteLogsAction, getLogsAction} from "./logs.actions";
import {getLogs} from "./logs.selector";
import {LogsState} from "./logs.state";
import {AddLogRequest} from "./addLogs/dto/add-log-request";
import {GetLogsRequest} from "./getLogs/dto/getLogs-request";

@Injectable({providedIn: 'root'})
export class LogFacade {
  stateGetLogsResponse$ = this.logsState.select(getLogs)

  constructor(
    private readonly logsState: Store<LogsState>
  ) {
  }

  getLogs(request: GetLogsRequest): void {
    this.logsState.dispatch(getLogsAction(request))
  }

  deleteLogs(): void {
    this.logsState.dispatch(deleteLogsAction())
  }

  addLog(request: AddLogRequest): void {
    this.logsState.dispatch(addLogAction(request));
  }

  deleteLog(request: GetLogsRequest): void {
    this.logsState.dispatch(deleteLogAction(request))
  }
}
