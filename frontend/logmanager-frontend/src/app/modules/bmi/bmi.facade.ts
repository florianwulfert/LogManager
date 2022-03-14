import {Injectable} from "@angular/core";
import {Store} from "@ngrx/store";
import {calcBmi} from "./bmi.selector";
import {calcBmiAction} from "./bmi.actions";
import {CalcBmiState} from "./calculate-bmi/store/calc-bmi.state";
import {CalculateBmiRequest} from "./calculate-bmi/dto/calculate-bmi-request";

@Injectable({providedIn: 'root'})
export class BmiFacade {
  stateCalcBmi$ = this.calBmiState.select(calcBmi)

  constructor(
    private readonly calBmiState: Store<CalcBmiState>,
  ) {
  }

  calcBmi(request: CalculateBmiRequest): void {
    this.calBmiState.dispatch(calcBmiAction(request));
  }


}
