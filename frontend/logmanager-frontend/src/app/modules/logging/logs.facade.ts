import {Injectable} from "@angular/core";
import {Store} from "@ngrx/store";
import {addLogAction, deleteLogAction, deleteLogsAction, getLogsAction} from "./logs.actions";
import {getLogs} from "./logs.selector";
import {LogsState} from "./logs.state";
import {AddLogRequest} from "./addLogs/dto/add-log-request";
import {DeleteLogRequest} from "./deleteLog/dto/delete-log-request";

@Injectable({providedIn: 'root'})
export class LogFacade {
  stateGetLogsResponse$ = this.logsState.select(getLogs)

  constructor(
    private readonly logsState: Store<LogsState>
  ) {
  }

  getLogs(): void {
    this.logsState.dispatch(getLogsAction())
  }

  deleteLogs(): void {
    this.logsState.dispatch(deleteLogsAction())
  }

  addLog(request: AddLogRequest): void {
    this.logsState.dispatch(addLogAction(request));
  }

  deleteLog(request: DeleteLogRequest): void {
    this.logsState.dispatch(deleteLogAction(request))
  }
}
