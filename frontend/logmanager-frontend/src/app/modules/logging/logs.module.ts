import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {StoreModule} from "@ngrx/store";
import {EffectsModule} from "@ngrx/effects";
import {LogEffects} from "./logs.effects";
import {LogFacade} from "./logs.facade";
import {LogsReducer} from "./logs.reducer";
import {LOGS_FEATURE_NAME} from "./logs.state";

@NgModule({
  declarations: [],
  imports: [
    CommonModule, StoreModule.forRoot({}),
    StoreModule.forFeature(LOGS_FEATURE_NAME, LogsReducer),
    EffectsModule.forRoot(), EffectsModule.forFeature([LogEffects])],
  providers: [LogFacade]
})
export class LogModule {
}
