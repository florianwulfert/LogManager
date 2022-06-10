import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {StoreModule} from "@ngrx/store";
import {EffectsModule} from "@ngrx/effects";
import {BibelleseEffects} from "./bibellese.effects";
import {BibelleseFacade} from "./bibellese.facade";
import {BibelleseReducer} from "./bibellese.reducer";
import {FeatureManager} from "../../../assets/utils/feature.manager";
import {BIBELLESE_FEATURE_NAME} from "./bibellese.state";

@NgModule({
  declarations: [],
  imports: [
    CommonModule, StoreModule.forRoot({}),
    StoreModule.forFeature(BIBELLESE_FEATURE_NAME, BibelleseReducer),
    EffectsModule.forRoot(), EffectsModule.forFeature([BibelleseEffects])],
  providers: [BibelleseFacade, FeatureManager]
})
export class BibelleseModule {
}
