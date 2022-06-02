import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {StoreModule} from "@ngrx/store";
import {EffectsModule} from "@ngrx/effects";
import {UpdateBibelleseEffects} from "./update-bibellese.effects";
import {UpdateBibelleseFacade} from "./update-bibellese.facade";
import {UpdateBibelleseReducer} from "./update-bibellese.reducer";
import {FeatureManager} from "../../../assets/utils/feature.manager";
import {UPDATE_BIBELLESE_FEATURE_NAME} from "./update-bibellese.state";

@NgModule({
  declarations: [],
  imports: [
    CommonModule, StoreModule.forRoot({}),
    StoreModule.forFeature(UPDATE_BIBELLESE_FEATURE_NAME, UpdateBibelleseReducer),
    EffectsModule.forRoot(), EffectsModule.forFeature([UpdateBibelleseEffects])],
  providers: [UpdateBibelleseFacade, FeatureManager]
})
export class UpdateBibelleseModule {
}
