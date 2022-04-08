import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {Action} from '@ngrx/store';
import {Observable, of} from 'rxjs';
import {map, switchMap} from 'rxjs/internal/operators';
import {catchError} from 'rxjs/operators';
import {BmiService} from "./bmi.service";
import {calcBmiAction, calcBmiResponseAction, loadCalcBmiErrorAction} from "./bmi.actions";
import {CalculateBmiRequest} from "./calculate-bmi/dto/calculate-bmi-request";

@Injectable({providedIn: 'root'})
export class BmiEffects {


  calculate$: Observable<Action> = createEffect(() =>
    this.actions$.pipe(
      ofType(calcBmiAction),
      switchMap((calcBmiRequest: CalculateBmiRequest) =>
        this.bmiService.calcBmi(calcBmiRequest).pipe(
          map((calcBmiResponse) => calcBmiResponseAction(calcBmiResponse)),
          catchError((error: string) => of(loadCalcBmiErrorAction({error})))
        )
      )
    )
  );

  constructor(private readonly actions$: Actions, private readonly bmiService: BmiService) {
  }
}
