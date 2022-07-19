import {HttpClient} from '@angular/common/http';
import {Injectable, OnDestroy} from '@angular/core';
import {Observable, Subject, throwError} from 'rxjs';
import {catchError, map} from "rxjs/operators";
import {ActorFacade} from "../actor/actor.facade";
import {FeatureManager} from "../../../assets/utils/feature.manager";
import {GetUserResponse} from "./getUser/get-user-response";
import {UserDto} from "../users/getUsers/user.dto";
import {GetUserRequest} from "./getUser/getUser-request";

const API_GET_USER = 'http://localhost:8081/user?name=';

@Injectable({
  providedIn: 'root'
})
export class UserService implements OnDestroy {
  constructor(private readonly http: HttpClient, private readonly actorFacade: ActorFacade, private featureManager: FeatureManager) {
  }

  ngOnDestroy() {
    this.onDestroy.next(null)
    this.onDestroy.complete()
  }

  onDestroy = new Subject()
  userDto: UserDto = {
    id: 0,
    name: "",
    birthdate: "",
    height: 0,
    weight: 0,
    bmi: 0,
    favouriteBookTitel: ""
  }

  getUser(request: GetUserRequest): Observable<GetUserResponse> {
    return this.http.get<GetUserResponse>(API_GET_USER + request.name, {
      observe: 'response'
    }).pipe(
      map((r) => {
        return r.body || {
          user: this.userDto
        }
      }),
      catchError((err) => {
        if (err.error instanceof Object) {
          this.featureManager.openSnackbar(err.error.text, "failed");
          return throwError('Wrong object in interface')
        } if (err.error) {
          this.featureManager.openSnackbar(err.error, "failed");
          return throwError('business error')
        }
        this.featureManager.openSnackbar('Due to technical issues it is currently not possible to request this user.', "failed")
        return throwError('Due to technical issues it is currently not possible to request this user.')
      })
    );
  }
}
