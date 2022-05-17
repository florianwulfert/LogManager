import {HttpClient} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {FindUserResponse} from "./find-user-response";
import {catchError, map} from "rxjs/operators";
import {FeatureManager} from "../../../assets/utils/feature.manager";
import {Injectable} from "@angular/core";

const API_FIND_USER = 'http://localhost:8081/validateUser?name='


@Injectable({
  providedIn: 'root'
})
export class ActorService {
  constructor(private readonly http: HttpClient, private featureManager: FeatureManager) {
  }

  getUserByName(name: string): Observable<FindUserResponse> {
    return this.http.get<FindUserResponse>(API_FIND_USER + name, {
      observe: 'response'
    }).pipe(
      map((r) => {
        return r.body || {
          foundUser: false
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
