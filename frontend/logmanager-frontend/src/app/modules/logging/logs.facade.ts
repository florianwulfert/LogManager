import {Injectable} from "@angular/core";
import {Store} from "@ngrx/store";
import {addLogAction, deleteLogAction, deleteLogsAction, getLogsAction} from "./logs.actions";
import {LogsGetState} from "./getLogs/store/logs-get.state";
import {addLog, deleteLog, deleteLogs, getLogs} from "./logs.selector";
import {LogsDeleteState} from "./deleteLogs/store/delete-logs.state";
import {AddLogRequest} from "./addLogs/dto/add-log-request";
import {LogAddState} from "./addLogs/store/log-add.state";
import {DeleteLogRequest} from "./deleteLog/dto/delete-log-request";
import {LogDeleteState} from "./deleteLog/store/log-delete.state";

@Injectable({providedIn: 'root'})
export class LogFacade {
  stateGetLogsResponse$ = this.logsGetState.select(getLogs)
  stateDeleteLogs$ = this.logsDeleteState.select(deleteLogs)
  stateAddLog$ = this.logAddState.select(addLog)
  stateDeleteLog$ = this.logDeleteState.select(deleteLog)

  constructor(
    private readonly logsGetState: Store<LogsGetState>,
    private readonly logsDeleteState: Store<LogsDeleteState>,
    private readonly logAddState: Store<LogAddState>,
    private readonly logDeleteState: Store<LogDeleteState>
  ) {
  }

  getLogs(): void {
    this.logsGetState.dispatch(getLogsAction())
  }

  deleteLogs(): void {
    this.logsDeleteState.dispatch(deleteLogsAction())
  }

  addLog(request: AddLogRequest): void {
    this.logAddState.dispatch(addLogAction(request));
  }

  deleteLog(request: DeleteLogRequest): void {
    this.logDeleteState.dispatch(deleteLogAction(request))
  }
}
