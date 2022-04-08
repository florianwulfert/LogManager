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

const API_GET_LOGS = 'http://localhost:8081/logs';
const API_DELETE_LOGS = 'http://localhost:8081/logs/delete';
const API_ADD_LOG = 'http://localhost:8081/log';
const API_DELETE_LOG = 'http://localhost:8081/logs/delete/'

@Injectable({
  providedIn: 'root'
})
export class LogService {
  constructor(private readonly http: HttpClient, private readonly actorFacade: ActorFacade, private featureManager: FeatureManager) {
  }

  name: string | undefined
  subscriptionManager = new SubscriptionManager();

  getLogs(): Observable<GetLogsResponse> {
    return this.http.get<GetLogsResponse>(API_GET_LOGS, {
      observe: 'response'
    }).pipe(
      map((r) => {
        return r.body || {
          result: [],
          returnMessage: ""
        }
      }),
      catchError((err) => {
        if(err.error instanceof Object) {
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
        if(err.error instanceof Object) {
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
        if(err.error instanceof Object) {
          this.featureManager.openSnackbar(err.error.text);
        } else {
          this.featureManager.openSnackbar(err.error);
        }
        return throwError('Due to technical issues it is currently not possible to add logs.');
      })
    );
  }

  deleteLog(i: number | undefined): Observable<DeleteLogResponse> {
    return this.http.delete<DeleteLogResponse>(API_DELETE_LOG + i, {
      observe: 'response'
    }).pipe(
      map((r) => {
        this.featureManager.openSnackbar("Log with the ID " + i + " was deleted.");
        return r.body || {
          result: [],
          returnMessage: ""
        }
      }),
      catchError((err) => {
        if(err.error instanceof Object) {
          this.featureManager.openSnackbar(err.error.text);
        } else {
          this.featureManager.openSnackbar(err.error);
        }
        return throwError('Due to technical issues it is currently not possible to delete this log.')
      })
    );
  }
}
