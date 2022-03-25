import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable, throwError} from 'rxjs';
import {GetUserResponse} from 'src/app/modules/user/getUser/dto/get-user-response';
import {catchError, map} from "rxjs/operators";
import {AddUserResponse} from "./addUser/dto/add-user-response";
import {AddUserRequest} from "./addUser/dto/add-user-request";
import {DeleteUserResponse} from "./deleteUser/dto/delete-user-response";
import {SubscriptionManager} from "../../../assets/utils/subscription.manager";
import {ActorFacade} from "../actor/actor.facade";
import {DeleteUsersResponse} from "./deleteUsers/dto/delete-users-response";
import {FeatureManager} from "../../../assets/utils/feature.manager";

const API_BASE = 'http://localhost:8081/users';
const API_ADD_USER = 'http://localhost:8081/user';
const API_DELETE_USERS = 'http://localhost:8081/user/delete';
const API_DELETE_USER = 'http://localhost:8081/user/delete/';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private readonly http: HttpClient, private readonly actorFacade: ActorFacade, private featureManager: FeatureManager) {
  }

  name: string | undefined
  subscriptionManager = new SubscriptionManager();

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
    this.subscriptionManager.add(this.actorFacade.stateActor$).subscribe(r => {
      this.name = r
    })
    return this.http.post<any>(API_ADD_USER, {...addUserRequest, actor: this.name}, {
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

  deleteUsers(): Observable<DeleteUsersResponse> {
    return this.http.delete<DeleteUsersResponse>(API_DELETE_USERS, {
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

  deleteUser(i: number | undefined): Observable<DeleteUserResponse> {
    this.subscriptionManager.add(this.actorFacade.stateActor$).subscribe(r => {
      this.name = r
    })
    return this.http.delete<DeleteUserResponse>(API_DELETE_USER + i + "?actor=" + this.name, {
      observe: 'response'
    }).pipe(
      map((r) => {
        console.log("User with ID " + i + " was successfully deleted.")
        this.featureManager.openSnackbar("User with ID " + i + " was successfully deleted.");
        return r.body || {
          result: []
        }
      }),
      catchError((err) => {
        if(err.error instanceof Object) {
          this.featureManager.openSnackbar(err.error.text);
        } else {
          this.featureManager.openSnackbar(err.error);
        }
        return throwError('Due to technical issues it is currently not possible to delete this user.');
      })
    );
  }
}
