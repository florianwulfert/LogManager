import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {StoreModule} from "@ngrx/store";
import {EffectsModule} from "@ngrx/effects";
import {UsersEffects} from "./users.effects";
import {UsersFacade} from "./users.facade";
import {USERS_FEATURE_NAME} from "./usersState";
import {UsersReducer} from "./users.reducer";
import {FeatureManager} from "../../../assets/utils/feature.manager";

@NgModule({
  declarations: [],
  imports: [
    CommonModule, StoreModule.forRoot({}),
    StoreModule.forFeature(USERS_FEATURE_NAME, UsersReducer),
    EffectsModule.forRoot(), EffectsModule.forFeature([UsersEffects])],
  providers: [UsersFacade, FeatureManager]
})
export class UsersModule {
}
