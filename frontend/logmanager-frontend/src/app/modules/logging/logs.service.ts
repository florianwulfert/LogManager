import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {map} from "rxjs/internal/operators";
import {catchError} from "rxjs/operators";
import {GetLogsResponse} from "./getLogs/dto/get-logs-response";

const API_GET_LOGS = 'http://localhost:8081/logs';

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
}
