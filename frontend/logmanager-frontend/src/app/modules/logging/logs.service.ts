import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {map} from "rxjs/internal/operators";
import {catchError} from "rxjs/operators";
import {GetLogsResponse} from "./getLogs/dto/get-logs-response";
import {DeleteLogsResponse} from "./deleteLogs/dto/delete-logs-response";

const API_GET_LOGS = 'http://localhost:8081/logs';
const API_DELETE_LOGS = 'http://localhost:8081/logs/delete';

@Injectable({
  providedIn: 'root'
})
export class LogService {
  constructor(private readonly http: HttpClient) {
  }

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
}
