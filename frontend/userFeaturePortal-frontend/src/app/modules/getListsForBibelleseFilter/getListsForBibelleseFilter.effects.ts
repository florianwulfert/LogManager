import {Injectable} from "@angular/core";
import {Observable, of} from "rxjs";
import {Action} from "@ngrx/store";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {catchError, map, switchMap} from "rxjs/operators";
import {
  getAllBibelleseAction,
  getAllBibelleseResponseAction,
  loadGetAllBibelleseErrorAction,
} from "./getListsForBibelleseFilter.actions";
import {GetListsForBibelleseFilterService} from "./getListsForBibelleseFilter.service";

@Injectable({providedIn: 'root'})
export class GetListsForBibelleseFilterEffects {

  getAllBibellese$: Observable<Action> = createEffect(() =>
    this.actions$.pipe(
      ofType(getAllBibelleseAction),
      switchMap(() =>
        this.getListsForBibelleseFilterService.getAllBibellese().pipe(
          map((getBibelleseResponse) => getAllBibelleseResponseAction(getBibelleseResponse)),
          catchError((error: string) => of(loadGetAllBibelleseErrorAction({error})))
        )
      )
    )
  );

  constructor(private readonly actions$: Actions, private readonly getListsForBibelleseFilterService: GetListsForBibelleseFilterService) {
  }
}
