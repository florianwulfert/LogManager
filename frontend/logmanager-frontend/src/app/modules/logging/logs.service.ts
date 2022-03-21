import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {map} from "rxjs/internal/operators";
import {catchError} from "rxjs/operators";
import {GetLogsResponse} from "./getLogs/dto/get-logs-response";
import {DeleteLogsResponse} from "./deleteLogs/dto/delete-logs-response";
import {AddLogResponse} from "./addLogs/dto/add-log-response";
import {AddLogRequest} from "./addLogs/dto/add-log-request";
import {SubscriptionManager} from "../../../assets/utils/subscription.manager";
import {ActorFacade} from "../actor/actor.facade";

const API_GET_LOGS = 'http://localhost:8081/logs';
const API_DELETE_LOGS = 'http://localhost:8081/logs/delete';
const API_ADD_LOG = 'http://localhost:8081/log';

@Injectable({
  providedIn: 'root'
})
export class LogService {
  constructor(private readonly http: HttpClient, private readonly actorFacade: ActorFacade) {
  }

  name: string | undefined
  subscriptionManager = new SubscriptionManager();

  getLogs(): Observable<GetLogsResponse> {
    return this.http.get<GetLogsResponse>(API_GET_LOGS, {
      observe: 'response'
    }).pipe(
      map((r) => {
        return r.body || {
          result: []
        }
      }),
      catchError(() => {
        return throwError('Due to technical issues it is currently not possible to request logs.');
      })
    );
  }

  deleteLogs(): Observable<DeleteLogsResponse> {
    return this.http.delete<DeleteLogsResponse>(API_DELETE_LOGS, {
      observe: 'response'
    }).pipe(
      map((r) => {
        return r.body || {
          result: ''
        }
      }),
      catchError(() => {
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
        console.log(r);
        return {
          result: r.body ? r.body : ''
        }
      }),
      catchError(() => {
        return throwError('Due to technical issues it is currently not possible to add logs.');
      })
    );
  }
}
