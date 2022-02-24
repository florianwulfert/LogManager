import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable, throwError} from 'rxjs';
import {GetUserResponse} from 'src/app/modules/user/dto/get-user-response';
import {map} from "rxjs/internal/operators";
import {catchError} from "rxjs/operators";
import {AddUserResponse} from "../dto/add-user-response";
import {AddUserRequest} from "../dto/add-user-request";

const API_BASE = 'http://localhost:8081/users';
const API_ADD_USER = 'http://localhost:8081/user';

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
        return r.body || {
          result: []
        }
      }),
      catchError(() => {
          return throwError('Due to technical issues it is currently not possible to request users.');
      })
    );
  }

  addUser(addUserRequest: AddUserRequest): Observable<AddUserResponse> {
    return this.http.post<AddUserResponse>(API_ADD_USER
      + "?actor=" + addUserRequest.actor
      + "&name=" + addUserRequest.name
      + "&birthdate=" + addUserRequest.birthdate
      + "&weight=" + addUserRequest.weight
      + "&height=" + addUserRequest.height
      + "&favouriteColor=" + addUserRequest.favouriteColor,{
      observe: 'body'
    }).pipe(
      map((r) => {
        debugger
        return r || {
          result: ''
        }
      }),
      catchError(() => {
        return throwError('Due to technical issues it is currently not possible to add users.');
      })
    );
  }
}
