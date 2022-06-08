import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {catchError, map} from "rxjs/operators";
import {UpdateBibelleseRequest} from "./update-bibellese-request";
import {ActorFacade} from "../actor/actor.facade";
import {FeatureManager} from "../../../assets/utils/feature.manager";
import {UpdateBibelleseResponse} from "./update-bibellese-response";

const API_BIBELLESE_UPDATE = 'http://localhost:8082/gelesen/update'

@Injectable({
  providedIn: 'root'
})
export class UpdateBibelleseService {
  constructor(private readonly http: HttpClient, private readonly actorFacade: ActorFacade, private featureManager: FeatureManager) {
  }

  name: string | undefined

  updateBibellese(updateBibelleseRequest: UpdateBibelleseRequest): Observable<UpdateBibelleseResponse> {
    console.log(updateBibelleseRequest)
    return this.http.post<any>(API_BIBELLESE_UPDATE, {...updateBibelleseRequest}, {
      observe: 'response'
    }).pipe(
      map((r) => {
        console.log(r.body)
        this.featureManager.openSnackbar(r.body?.returnMessage, "success");
        return r.body || {
          result: [],
          returnMessage: ''
        }
      }),
      catchError((err) => {
        if(err.error instanceof Object) {
          this.featureManager.openSnackbar(err.error.text, "failed");
        } else {
          this.featureManager.openSnackbar(err.error, "failed");
        }
        return throwError('Due to technical issues it is currently not possible to update entry for Bibellese.');
      })
    );
  }
}
