import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {StoreModule} from "@ngrx/store";
import {EffectsModule} from "@ngrx/effects";
import {GET_LOGS_FEATURE_NAME} from "./logs.state";
import {LogsGetReducer} from "./getLogs/store/logs-get.reducer";
import {LogEffects} from "./logs.effects";
import {LogFacade} from "./logs.facade";
import {LogsDeleteReducer} from "./deleteLogs/store/delete-logs.reducer";
import {DELETE_LOGS_FEATURE_NAME} from "./deleteLogs/store/delete-logs.state";
import {ADD_LOG_FEATURE_NAME} from "./addLogs/store/log-add.state";
import {LogAddReducer} from "./addLogs/store/log-add.reducer";
import {DELETE_LOG_FEATURE_NAME} from "./deleteLog/store/log-delete.state";
import {LogDeleteReducer} from "./deleteLog/store/log-delete.reducer";

@NgModule({
  declarations: [],
  imports: [
    CommonModule, StoreModule.forRoot({}),
    StoreModule.forFeature(GET_LOGS_FEATURE_NAME, LogsGetReducer),
    StoreModule.forFeature(DELETE_LOGS_FEATURE_NAME, LogsDeleteReducer),
    StoreModule.forFeature(ADD_LOG_FEATURE_NAME, LogAddReducer),
    StoreModule.forFeature(DELETE_LOG_FEATURE_NAME, LogDeleteReducer),
    EffectsModule.forRoot(), EffectsModule.forFeature([LogEffects])],
  providers: [LogFacade]
})
export class LogModule {
}
