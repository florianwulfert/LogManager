import {createFeatureSelector, createSelector} from "@ngrx/store";
import {CALC_BMI_FEATURE_NAME, CalcBmiState} from "./calculate-bmi/store/calc-bmi.state";

const calcBmiState = createFeatureSelector<CalcBmiState>(CALC_BMI_FEATURE_NAME);
export const calcBmi = createSelector(calcBmiState, (state: CalcBmiState): string => state.response);
