import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable, throwError} from 'rxjs';
import {GetUserResponse} from 'src/app/modules/user/dto/get-user-response';
import {map} from "rxjs/internal/operators";
import {catchError} from "rxjs/operators";

const API_BASE = 'http://localhost:8081/users';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private readonly http: HttpClient) {}

  getUsers(): Observable<GetUserResponse> {
    return this.http.get<GetUserResponse>(API_BASE, {
      observe: 'response'
    }).pipe(
      map((r) => {
        console.log(r);
        return r.body || {
          result: []
        }
      }),
      catchError(() => {
          return throwError('Zurzeit ist eine Abfragen der Nutzer technischen Gründen nicht möglich.');
      })
    );
  }
}
