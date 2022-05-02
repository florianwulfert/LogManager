import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {catchError, map} from "rxjs/operators";
import {GetLogsResponse} from "./getLogs/dto/get-logs-response";
import {DeleteLogsResponse} from "./deleteLogs/dto/delete-logs-response";
import {AddLogResponse} from "./addLogs/dto/add-log-response";
import {AddLogRequest} from "./addLogs/dto/add-log-request";
import {SubscriptionManager} from "../../../assets/utils/subscription.manager";
import {ActorFacade} from "../actor/actor.facade";
import {FeatureManager} from "../../../assets/utils/feature.manager";
import {DeleteLogResponse} from "./deleteLog/dto/delete-log-response";
import {GetLogsRequest} from "./getLogs/dto/getLogs-request";
import {DeleteLogRequest} from "./deleteLog/dto/delete-log-request";

const API_GET_LOGS = 'http://localhost:8081/logs';
const API_DELETE_LOGS = 'http://localhost:8081/logs';
const API_ADD_LOG = 'http://localhost:8081/log';
const API_DELETE_LOG = 'http://localhost:8081/log/id/'

@Injectable({
  providedIn: 'root'
})
export class LogService {
  constructor(private readonly http: HttpClient, private readonly actorFacade: ActorFacade, private featureManager: FeatureManager) {
  }

  name: string | undefined
  subscriptionManager = new SubscriptionManager();
  countParameter: number = 0

  checkParameter(requestParameter: string | undefined, parameterName: string): string {

    if (requestParameter === "" || requestParameter === null || requestParameter === undefined) {
      return ""
    } else {
      this.countParameter++
      console.log(this.countParameter)
      let connectionItem = this.countParameter > 1 ? "&" : "?"
      return connectionItem + parameterName + '=' + requestParameter
    }
  }

  checkDateTime(dateTime: string): string {
    if (dateTime === '' || dateTime === null) {
      return ''
    } else {
      return dateTime + "-00-00-00"
    }
  }

  buildGetLogsRequestParams(getLogsRequest: GetLogsRequest): String {
    let severity: string
    let message: string
    let startDateTime: string
    let endDateTime: string
    let user: string

    severity = this.checkParameter(getLogsRequest.severity, "severity")
    message = this.checkParameter(getLogsRequest.message, "message")
    startDateTime = this.checkParameter(getLogsRequest.startDateTime, "startDateTime")
    endDateTime = this.checkParameter(getLogsRequest.endDateTime, "endDateTime")
    let startDate = this.checkDateTime(startDateTime)
    let endDate = this.checkDateTime(endDateTime)
    user = this.checkParameter(getLogsRequest.user?.name, "user")

    return severity + message + startDate + endDate + user
  }

  buildDeleteLogRequestParams(deleteLogRequest: DeleteLogRequest): String {
    let severity: string
    let message: string
    let startDateTime: string
    let endDateTime: string
    let user: string

    severity = this.checkParameter(deleteLogRequest.severity, "severity")
    message = this.checkParameter(deleteLogRequest.message, "message")
    startDateTime = this.checkParameter(deleteLogRequest.startDateTime, "startDateTime")
    endDateTime = this.checkParameter(deleteLogRequest.endDateTime, "endDateTime")
    let startDate = this.checkDateTime(startDateTime)
    let endDate = this.checkDateTime(endDateTime)
    user = this.checkParameter(deleteLogRequest.user?.name, "user")

    return severity + message + startDate + endDate + user
  }

  getLogs(getLogsRequest: GetLogsRequest): Observable<GetLogsResponse> {
    console.log(getLogsRequest)
    this.countParameter = 0
    return this.http.get<GetLogsResponse>(API_GET_LOGS + this.buildGetLogsRequestParams(getLogsRequest), {
      observe: 'response'
    }).pipe(
      map((r) => {
        console.log(r.body)
        return r.body || {
          result: [],
          returnMessage: ""
        }
      }),
      catchError((err) => {
        if (err.error instanceof Object) {
          this.featureManager.openSnackbar(err.error.text);
        } else {
          this.featureManager.openSnackbar(err.error);
        }
        return throwError('Due to technical issues it is currently not possible to request logs.');
      })
    );
  }

  deleteLogs(): Observable<DeleteLogsResponse> {
    return this.http.delete<DeleteLogsResponse>(API_DELETE_LOGS, {
      observe: 'response'
    }).pipe(
      map((r) => {
        this.featureManager.openSnackbar(r.body?.returnMessage);
        return r.body || {
          result: [],
          returnMessage: ''
        }
      }),
      catchError((err) => {
        if (err.error instanceof Object) {
          this.featureManager.openSnackbar(err.error.text);
        } else {
          this.featureManager.openSnackbar(err.error);
        }
        return throwError('Due to technical issues it is currently not possible to delete logs.');
      })
    );
  }

  addLog(addLogRequest: AddLogRequest): Observable<AddLogResponse> {
    this.subscriptionManager.add(this.actorFacade.stateActor$).subscribe(r => {
      this.name = r
    })
    return this.http.post<any>(API_ADD_LOG, {...addLogRequest, user: this.name}, {
      observe: 'response'
    }).pipe(
      map((r) => {
        this.featureManager.openSnackbar(r.body?.returnMessage);
        return r.body || {
          result: [],
          returnMessage: ''
        }
      }),
      catchError((err) => {
        if (err.error instanceof Object) {
          this.featureManager.openSnackbar(err.error.text);
        } else {
          this.featureManager.openSnackbar(err.error);
        }
        return throwError('Due to technical issues it is currently not possible to add logs.');
      })
    );
  }

  deleteLog(deleteLogRequest: DeleteLogRequest): Observable<DeleteLogResponse> {
    this.countParameter = 0
    return this.http.delete<DeleteLogResponse>(API_DELETE_LOG + deleteLogRequest.id + this.buildDeleteLogRequestParams(deleteLogRequest), {
      observe: 'response'
    }).pipe(
      map((r) => {
        this.featureManager.openSnackbar("Log with the ID " + deleteLogRequest.id + " was deleted.");
        return r.body || {
          result: [],
          returnMessage: ""
        }
      }),
      catchError((err) => {
        if (err.error instanceof Object) {
          this.featureManager.openSnackbar(err.error.text);
        } else {
          this.featureManager.openSnackbar(err.error);
        }
        return throwError('Due to technical issues it is currently not possible to delete this log.')
      })
    );
  }
}
