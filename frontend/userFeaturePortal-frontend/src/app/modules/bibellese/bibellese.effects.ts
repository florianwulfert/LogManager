import {Injectable} from "@angular/core";
import {Observable, of} from "rxjs";
import {Action} from "@ngrx/store";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {catchError, map, switchMap} from "rxjs/operators";
import {
  addBibelleseAction,
  addBibelleseResponseAction,
  deleteBibelleseAction,
  deleteBibelleseResponseAction,
  getBibelleseAction,
  getBibelleseResponseAction,
  loadAddBibelleseErrorAction,
  loadDeleteBibelleseErrorAction,
  loadGetBibelleseErrorAction,
} from "./bibellese.actions";
import {BibelleseService} from "./bibellese.service";
import {AddBibelleseRequest} from "./addBibellese/add-bibellese-request";
import {DeleteBibelleseRequest} from "./deleteBibellese/delete-bibellese-request";
import {GetBibelleseRequest} from "./getBibellese/get-bibellese-request";

@Injectable({providedIn: 'root'})
export class BibelleseEffects {
  get$: Observable<Action> = createEffect(() =>
    this.actions$.pipe(
      ofType(getBibelleseAction),
      switchMap((request: GetBibelleseRequest) =>
        this.bibelleseService.getBibellese(request).pipe(
          map((getBibelleseResponse) => getBibelleseResponseAction(getBibelleseResponse)),
          catchError((error: string) => of(loadGetBibelleseErrorAction({error})))
        )
      )
    )
  );

  add$: Observable<Action> = createEffect(() =>
    this.actions$.pipe(
      ofType(addBibelleseAction),
      switchMap((addBibelleseRequest: AddBibelleseRequest) =>
        this.bibelleseService.addBibellese(addBibelleseRequest).pipe(
          map((addBibelleseResponse) => addBibelleseResponseAction(addBibelleseResponse)),
          catchError((error: string) => of(loadAddBibelleseErrorAction({error})))
        )
      )
    )
  );

  delete$: Observable<Action> = createEffect(() =>
    this.actions$.pipe(
      ofType(deleteBibelleseAction),
      switchMap((deleteBibelleseRequest: DeleteBibelleseRequest) =>
        this.bibelleseService.deleteBibellese(deleteBibelleseRequest.id).pipe(
          map((deleteBibelleseResponse) =>
            deleteBibelleseResponseAction(deleteBibelleseResponse)),
          catchError((error: string) => of(loadDeleteBibelleseErrorAction({error})))
        )
      )
    )
  );

  constructor(private readonly actions$: Actions, private readonly bibelleseService: BibelleseService) {
  }
}
