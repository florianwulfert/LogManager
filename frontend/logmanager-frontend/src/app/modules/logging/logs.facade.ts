import {Injectable} from "@angular/core";
import {Store} from "@ngrx/store";
import {getLogsAction} from "./logs.actions";
import {LogsGetState} from "./getLogs/store/logs-get.state";
import {getLogs} from "./logs.selector";

@Injectable({providedIn: 'root'})
export class LogFacade {
  stateGetLogsResponse$ = this.logsGetState.select(getLogs)

  constructor(private readonly logsGetState: Store<LogsGetState>) {
  }

  getLogs(): void {
    this.logsGetState.dispatch(getLogsAction());
  }
}
