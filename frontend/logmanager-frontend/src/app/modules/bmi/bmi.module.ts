import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {StoreModule} from "@ngrx/store";
import {EffectsModule} from "@ngrx/effects";
import {BmiEffects} from "./bmi.effects";
import {BmiFacade} from "./bmi.facade";
import {CalcBmiReducer} from "./calculate-bmi/store/calc-bmi.reducer";
import {CALC_BMI_FEATURE_NAME} from "./calculate-bmi/store/calc-bmi.state";

@NgModule({
  declarations: [],
  imports: [
    CommonModule, StoreModule.forRoot({}),
    StoreModule.forFeature(CALC_BMI_FEATURE_NAME, CalcBmiReducer),
    EffectsModule.forRoot(), EffectsModule.forFeature([BmiEffects])],
  providers: [BmiFacade]
})
export class BmiModule {
}
