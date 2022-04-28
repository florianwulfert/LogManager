import {createAction, props} from '@ngrx/store';
import {CalculateBmiRequest} from "./calculate-bmi/dto/calculate-bmi-request";
import {CalculateBmiErrorResponse, CalculateBmiResponse} from "./calculate-bmi/dto/calc-bmi-response";


export const calcBmiAction = createAction('Calculate BMI', props<CalculateBmiRequest>());
export const calcBmiResponseAction = createAction('Get response if BMI calculation succeed', props<CalculateBmiResponse>());
export const loadCalcBmiErrorAction = createAction('Load Calculate BMI failure', props<CalculateBmiErrorResponse>());
