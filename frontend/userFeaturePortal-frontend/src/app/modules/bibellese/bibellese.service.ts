import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {catchError, map} from "rxjs/operators";
import {ActorFacade} from "../actor/actor.facade";
import {FeatureManager} from "../../../assets/utils/feature.manager";
import {GetBibelleseResponse} from "./getBibellese/get-bibellese-response";
import {GetBibelleseRequest} from "./getBibellese/get-bibellese-request";
import {AddBibelleseRequest} from "./addBibellese/add-bibellese-request";
import {AddBibelleseResponse} from "./addBibellese/add-bibellese-response";
import {DeleteBibelleseResponse} from "./deleteBibellese/delete-bibellese-response";

const API_BIBELLESE = 'http://localhost:8082/gelesen'

@Injectable({
  providedIn: 'root'
})
export class BibelleseService {
  constructor(private readonly http: HttpClient, private readonly actorFacade: ActorFacade, private featureManager: FeatureManager) {
  }

  name: string | undefined
  countParameter: number = 0

  checkParameter(requestParameter: string | undefined, parameterName: string): string {
    if (requestParameter === "" || requestParameter === null || requestParameter === undefined) {
      return ""
    } else {
      this.countParameter++
      let connectionItem = this.countParameter > 1 ? "&" : "?"
      return connectionItem + parameterName + '=' + requestParameter
    }
  }

  buildGetBibelleseRequestParams(getBibelleseRequest: GetBibelleseRequest): String {
    let bibelabschnitt: string
    let kommentarAusschnitt: string
    let leser: string
    let label: string
    let lieblingsvers: string

    bibelabschnitt = this.checkParameter(getBibelleseRequest.bibelabschnitt, "bibelabschnitt")
    kommentarAusschnitt = this.checkParameter(getBibelleseRequest.kommentarAusschnitt, "kommentarAusschnitt")
    leser = this.checkParameter(getBibelleseRequest.leser, "leser")
    label = this.checkParameter(getBibelleseRequest.label, "label")
    lieblingsvers = this.checkParameter(getBibelleseRequest.lieblingsvers, "lieblingsvers")

    return bibelabschnitt + kommentarAusschnitt + leser + label + lieblingsvers
  }

  getBibellese(request: GetBibelleseRequest): Observable<GetBibelleseResponse> {
    return this.http.get<GetBibelleseResponse>(API_BIBELLESE + this.buildGetBibelleseRequestParams(request), {
      observe: 'response'
    }).pipe(
      map((r) => {
        this.featureManager.openSnackbar(r.body?.returnMessage);
        return r.body || {
          result: [],
          returnMessage: ''
        }
      }),
      catchError((err) => {
        if (err.error instanceof Object) {
          this.featureManager.openSnackbar(err.error.text);
        } else {
          this.featureManager.openSnackbar(err.error);
        }
        return throwError('Due to technical issues it is currently not possible to request Bibellese.');
      })
    );
  }

  addBibellese(addBibelleseRequest: AddBibelleseRequest): Observable<AddBibelleseResponse> {
    return this.http.post<any>(API_BIBELLESE, {...addBibelleseRequest}, {
      observe: 'response'
    }).pipe(
      map((r) => {
        this.featureManager.openSnackbar(r.body?.returnMessage);
        return r.body || {
          result: [],
          returnMessage: ''
        }
      }),
      catchError((err) => {
        if(err.error instanceof Object) {
          this.featureManager.openSnackbar(err.error.text);
        } else {
          this.featureManager.openSnackbar(err.error);
        }
        return throwError('Due to technical issues it is currently not possible to add entry for Bibellese.');
      })
    );
  }

  deleteBibellese(i: string | undefined): Observable<DeleteBibelleseResponse> {
    return this.http.delete<DeleteBibelleseResponse>(API_BIBELLESE + "/" + i, {
      observe: 'response'
    }).pipe(
      map((r) => {
        this.featureManager.openSnackbar("Bibellese-Entry with the ID " + i + " was deleted.");
        return r.body || {
          result: [],
          returnMessage: ""
        }
      }),
      catchError((err) => {
        if(err.error instanceof Object) {
          this.featureManager.openSnackbar(err.error.text);
        } else {
          this.featureManager.openSnackbar(err.error);
        }
        return throwError('Due to technical issues it is currently not possible to delete this entry for Bibellese.')
      })
    );
  }
}
