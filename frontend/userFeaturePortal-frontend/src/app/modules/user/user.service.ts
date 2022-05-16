import {HttpClient} from '@angular/common/http';
import {Injectable, OnDestroy} from '@angular/core';
import {Observable, Subject, throwError} from 'rxjs';
import {catchError, map} from "rxjs/operators";
import {ActorFacade} from "../actor/actor.facade";
import {FeatureManager} from "../../../assets/utils/feature.manager";
import {GetUserResponse} from "./getUser/get-user-response";

const API_GET_USER = 'http://localhost:8081/user';

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

  name: string | undefined
  onDestroy = new Subject()

  getUser(): Observable<GetUserResponse> {
    return this.http.get<GetUserResponse>(API_GET_USER, {
      observe: 'response'
    }).pipe(
      map((r) => {
        let UserDto;
        return r.body || {
          result: UserDto = {
            id: 0,
            name: "",
            birthdate: "",
            height: 0,
            weight: 0,
            bmi: 0,
            favouriteBookTitel: ""
          }
        }
      }),
      catchError((err) => {
        if (err.error instanceof Object) {
          this.featureManager.openSnackbar(err.error.text);
        } else {
          this.featureManager.openSnackbar(err.error);
        }
        return throwError('Due to technical issues it is currently not possible to request this user.');
      })
    );
  }

}
