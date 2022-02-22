import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {StoreModule} from "@ngrx/store";
import {USER_FEATURE_NAME} from "./store/user-state";
import {UserReducer} from "./store/user.reducer";
import {EffectsModule} from "@ngrx/effects";
import {UserEffects} from "./store/user.effects";
import {UserFacade} from "./store/user.facade";


@NgModule({
  declarations: [],
  imports: [
    CommonModule, StoreModule.forRoot({}), StoreModule.forFeature(USER_FEATURE_NAME, UserReducer),
    EffectsModule.forRoot(), EffectsModule.forFeature([UserEffects])],
    providers: [UserFacade]
})
export class UserModule { }
