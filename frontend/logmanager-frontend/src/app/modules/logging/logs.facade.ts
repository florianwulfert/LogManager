import {Injectable} from "@angular/core";
import {Store} from "@ngrx/store";
import {addLogAction, deleteLogsAction, getLogsAction} from "./logs.actions";
import {LogsGetState} from "./getLogs/store/logs-get.state";
import {addLog, deleteLogs, getLogs} from "./logs.selector";
import {LogsDeleteState} from "./deleteLogs/store/delete-logs.state";
import {AddLogRequest} from "./addLogs/dto/add-log-request";
import {LogAddState} from "./addLogs/store/log-add.state";

@Injectable({providedIn: 'root'})
export class LogFacade {
  stateGetLogsResponse$ = this.logsGetState.select(getLogs)
  stateDeleteLogs$ = this.logsDeleteState.select(deleteLogs)
  stateAddLog$ = this.logAddState.select(addLog)

  constructor(
    private readonly logsGetState: Store<LogsGetState>,
    private readonly logsDeleteState: Store<LogsDeleteState>,
    private readonly logAddState: Store<LogAddState>
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
}
