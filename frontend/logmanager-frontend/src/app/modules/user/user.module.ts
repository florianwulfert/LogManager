import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {StoreModule} from "@ngrx/store";
import {EffectsModule} from "@ngrx/effects";
import {UserEffects} from "./user.effects";
import {UserFacade} from "./user.facade";
import {GET_USER_FEATURE_NAME} from "./getUser/store/user-get.state";
import {ADD_USER_FEATURE_NAME} from "./addUser/store/user-add.state";
import {UserGetReducer} from "./getUser/store/user-get.reducer";
import {UserAddReducer} from "./addUser/store/user-add.reducer";
import {FeatureManager} from "../../../assets/utils/feature.manager";

@NgModule({
  declarations: [],
  imports: [
    CommonModule, StoreModule.forRoot({}),
    StoreModule.forFeature(GET_USER_FEATURE_NAME, UserGetReducer),
    StoreModule.forFeature(ADD_USER_FEATURE_NAME, UserAddReducer),
    EffectsModule.forRoot(), EffectsModule.forFeature([UserEffects])],
  providers: [UserFacade, FeatureManager]
})
export class UserModule {
}
