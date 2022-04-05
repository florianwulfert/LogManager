import {Injectable} from "@angular/core";
import {Store} from "@ngrx/store";
import {addLogAction, deleteLogAction, deleteLogsAction, getLogsAction} from "./logs.actions";
import {LogsGetState} from "./getLogs/store/logs-get.state";
import {addLog, deleteLog, deleteLogs, getLogs} from "./logs.selector";
import {LogsDeleteState} from "./deleteLogs/store/delete-logs.state";
import {addLogAction, deleteLogsAction, getLogsAction} from "./logs.actions";
import {LogsState} from "./logs.state";
import {getLogs} from "./logs.selector";
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
  stateGetLogsResponse$ = this.logsState.select(getLogs)

  constructor(
    private readonly logsState: Store<LogsState>
  ) {}

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
    this.logDeleteState.dispatch(deleteLogAction(request))
  }
}
