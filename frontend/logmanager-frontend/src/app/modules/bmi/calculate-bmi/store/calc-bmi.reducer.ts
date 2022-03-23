import {createReducer, on} from "@ngrx/store";
import {CalculateBmiResponse} from "../dto/calc-bmi-response";
import {calcBmiAction, calcBmiResponseAction} from "../../bmi.actions";
import {CALC_BMI_INITIAL_STATE, CalcBmiState} from "./calc-bmi.state";

const handleCalcBmiResponse = (state: CalcBmiState, resp: CalculateBmiResponse): CalcBmiState => {
  return {
    ...state,
    response: resp.result,
  };
}

const handleCalcBmi = (state: CalcBmiState): CalcBmiState => {
  return {
    ...state,
  };
};

export const CalcBmiReducer = createReducer(
  CALC_BMI_INITIAL_STATE,
  on(calcBmiResponseAction, handleCalcBmiResponse),
  on(calcBmiAction, handleCalcBmi)
)
