import {Injectable} from "@angular/core";
import {Observable, of} from "rxjs";
import {Action} from "@ngrx/store";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {catchError, map, switchMap} from "rxjs/operators";
import {
  loadUpdateBibelleseErrorAction,
  updateBibelleseAction,
  updateBibelleseResponseAction
} from "./update-bibellese.actions";
import {UpdateBibelleseService} from "./update-bibellese.service";
import {UpdateBibelleseRequest} from "./update-bibellese-request";

@Injectable({providedIn: 'root'})
export class UpdateBibelleseEffects {

  update$: Observable<Action> = createEffect(() =>
    this.actions$.pipe(
      ofType(updateBibelleseAction),
      switchMap((updateBibelleseRequest: UpdateBibelleseRequest) =>
        this.updateBibelleseService.updateBibellese(updateBibelleseRequest).pipe(
          map((updateBibelleseResponse) => updateBibelleseResponseAction(updateBibelleseResponse)),
          catchError((error: string) => of(loadUpdateBibelleseErrorAction({error})))
        )
      )
    )
  );

  constructor(private readonly actions$: Actions, private readonly updateBibelleseService: UpdateBibelleseService) {
  }
}
