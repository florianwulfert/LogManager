import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {StoreModule} from "@ngrx/store";
import {EffectsModule} from "@ngrx/effects";
import {GET_LOGS_FEATURE_NAME} from "./getLogs/store/logs-get.state";
import {LogsGetReducer} from "./getLogs/store/logs-get.reducer";
import {LogEffects} from "./logs.effects";
import {LogsFacade} from "./logs.facade";

@NgModule({
  declarations: [],
  imports: [
    CommonModule, StoreModule.forRoot({}),
    StoreModule.forFeature(GET_LOGS_FEATURE_NAME, LogsGetReducer),
    EffectsModule.forRoot(), EffectsModule.forFeature([LogEffects])],
  providers: [LogsFacade]
})
export class LogModule {
}