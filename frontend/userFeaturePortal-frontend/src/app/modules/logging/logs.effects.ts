import {Injectable} from "@angular/core";
import {Observable, of} from "rxjs";
import {Action} from "@ngrx/store";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {catchError, map, switchMap} from "rxjs/operators";
import {
  addLogAction,
  addLogResponseAction,
  deleteLogAction,
  deleteLogResponseAction,
  deleteLogsAction,
  deleteLogsResponseAction,
  getLogsAction,
  getLogsResponseAction,
  loadAddLogErrorAction,
  loadDeleteLogErrorAction,
  loadDeleteLogsErrorAction,
  loadGetLogsErrorAction
} from "./logs.actions";
import {LogService} from "./logs.service";
import {AddLogRequest} from "./addLogs/dto/add-log-request";
import {DeleteLogRequest} from "./deleteLog/dto/delete-log-request";
import {GetLogsRequest} from "./getLogs/dto/getLogs-request";


@Injectable({providedIn: 'root'})
export class LogEffects {
  get$: Observable<Action> = createEffect(() =>
    this.actions$.pipe(
      ofType(getLogsAction),
      switchMap((getLogsRequest: GetLogsRequest) =>
        this.logService.getLogs(getLogsRequest).pipe(
          map((getLogsResponse) => getLogsResponseAction(getLogsResponse)),
          catchError((error: string) => of(loadGetLogsErrorAction({error})))
        )
      )
    )
  );

  delete$: Observable<Action> = createEffect(() =>
    this.actions$.pipe(
      ofType(deleteLogsAction),
      switchMap(() =>
        this.logService.deleteLogs().pipe(
          map((deleteLogsResponse) => deleteLogsResponseAction(deleteLogsResponse)),
          catchError((error: string) => of(loadDeleteLogsErrorAction({error})))
        )
      )
    )
  );

  add$: Observable<Action> = createEffect(() =>
    this.actions$.pipe(
      ofType(addLogAction),
      switchMap((addLogRequest: AddLogRequest) =>
        this.logService.addLog(addLogRequest).pipe(
          map((addLogResponse) => addLogResponseAction(addLogResponse)),
          catchError((error: string) => of(loadAddLogErrorAction({error})))
        )
      )
    )
  );

  deleteLog$: Observable<Action> = createEffect(() =>
    this.actions$.pipe(
      ofType(deleteLogAction),
      switchMap((deleteLogRequest: DeleteLogRequest) =>
        this.logService.deleteLog(deleteLogRequest).pipe(
          map((deleteLogResponse) => deleteLogResponseAction(deleteLogResponse)),
          catchError((error: string) => of(loadDeleteLogErrorAction({error})))
        )
      )
    )
  );

  constructor(private readonly actions$: Actions, private readonly logService: LogService) {
  }
}
