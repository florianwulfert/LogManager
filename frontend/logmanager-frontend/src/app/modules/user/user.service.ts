import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable, throwError} from 'rxjs';
import {GetUserResponse} from 'src/app/modules/user/getUser/dto/get-user-response';
import {map} from "rxjs/internal/operators";
import {catchError} from "rxjs/operators";
import {AddUserResponse} from "./addUser/dto/add-user-response";
import {AddUserRequest} from "./addUser/dto/add-user-request";
import {DeleteUserResponse} from "./deleteUser/dto/delete-user-response";

const API_BASE = 'http://localhost:8081/users';
const API_ADD_USER = 'http://localhost:8081/user';
const API_DELETE_USER = 'http://localhost:8081/user/delete';


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
    return this.http.post<any>(API_ADD_USER, {...addUserRequest,actor: 'Peter'},{
      observe: 'response'
    }).pipe(
      map((r) => {
        console.log(r);
        return {
          result: r.body ? r.body : ''
        }
      }),
      catchError(() => {
        return throwError('Due to technical issues it is currently not possible to add users.');
      })
    );
  }

  deleteUser(): Observable<DeleteUserResponse> {
    return this.http.delete<DeleteUserResponse>(API_DELETE_USER, {
      observe: 'response'
    }).pipe(
      map((r) => {
        return r.body || {
          result: ''
        }
      }),
      catchError(() => {
        return throwError('Due to technical issues it is currently not possible to delete users.');
      })
    );
  }
}
