import {Injectable} from "@angular/core";
import {Store} from "@ngrx/store";
import {addLogAction, deleteLogsAction, getLogsAction} from "./logs.actions";
import {LogsState} from "./logs.state";
import {getLogs} from "./logs.selector";
import {AddLogRequest} from "./addLogs/dto/add-log-request";

@Injectable({providedIn: 'root'})
export class LogFacade {
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
}
