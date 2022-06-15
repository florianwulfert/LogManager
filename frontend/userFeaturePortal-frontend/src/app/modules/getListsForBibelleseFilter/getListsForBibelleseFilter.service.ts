import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {catchError, map} from "rxjs/operators";
import {ActorFacade} from "../actor/actor.facade";
import {FeatureManager} from "../../../assets/utils/feature.manager";
import {GetListsForFilterBibelleseResponse} from "./getListsForFilterBibellese-response";
import {GetListsDto} from "./getLists.dto";

const API_BIBELLESE_ALL = 'http://localhost:8082/gelesen/all'


@Injectable({
  providedIn: 'root'
})
export class GetListsForBibelleseFilterService {
  constructor(private readonly http: HttpClient, private readonly actorFacade: ActorFacade, private featureManager: FeatureManager) {
  }

  name: string | undefined
  getListsDto: GetListsDto = {
    bibelabschnitte: [],
    labels: [],
    lieblingsverse: []
  }

  getAllBibellese(): Observable<GetListsForFilterBibelleseResponse> {
    return this.http.get<GetListsForFilterBibelleseResponse>(API_BIBELLESE_ALL, {
      observe: 'response'
    }).pipe(
      map((r) => {
        console.log(r.body)
        return r.body || {
          result: this.getListsDto
        }
      }),
      catchError((err) => {
        if(err.error instanceof Object) {
          this.featureManager.openSnackbar(err.error.text, "failed");
        } else {
          console.error(err.error)
          this.featureManager.openSnackbar(err.error, "failed");
        }
        return throwError('Due to technical issues it is currently not possible to request Bibellese.');
      })
    );
  }
}
