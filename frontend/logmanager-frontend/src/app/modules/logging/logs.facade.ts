import {Injectable} from "@angular/core";
import {Store} from "@ngrx/store";
import {deleteLogsAction, getLogsAction} from "./logs.actions";
import {LogsGetState} from "./getLogs/store/logs-get.state";
import {deleteLogs, getLogs} from "./logs.selector";
import {LogsDeleteState} from "./deleteLogs/store/delete-logs.state";

@Injectable({providedIn: 'root'})
export class LogFacade {
  stateGetLogsResponse$ = this.logsGetState.select(getLogs)
  stateDeleteLogs$ = this.logsDeleteState.select(deleteLogs)

  constructor(
    private readonly logsGetState: Store<LogsGetState>,
    private readonly logsDeleteState: Store<LogsDeleteState>
  ) {
  }

  getLogs(): void {
    this.logsGetState.dispatch(getLogsAction())
  }

  deleteLogs(): void {
    this.logsDeleteState.dispatch(deleteLogsAction())
  }
}
