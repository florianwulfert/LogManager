import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {StoreModule} from "@ngrx/store";
import {EffectsModule} from "@ngrx/effects";
import {GetListsForBibelleseFilterEffects} from "./getListsForBibelleseFilter.effects";
import {GetListsForBibelleseFilterFacade} from "./getListsForBibelleseFilter.facade";
import {GetListsForBibelleseFilterReducer} from "./getListsForBibelleseFilter.reducer";
import {FeatureManager} from "../../../assets/utils/feature.manager";
import {GET_LISTS_FOR_FILTER_BIBELLESE_FEATURE_NAME} from "./getListsForBibelleseFilter.state";

@NgModule({
  declarations: [],
  imports: [
    CommonModule, StoreModule.forRoot({}),
    StoreModule.forFeature(GET_LISTS_FOR_FILTER_BIBELLESE_FEATURE_NAME, GetListsForBibelleseFilterReducer),
    EffectsModule.forRoot(), EffectsModule.forFeature([GetListsForBibelleseFilterEffects])],
  providers: [GetListsForBibelleseFilterFacade, FeatureManager]
})
export class GetListsForBibelleseFilterModule {
}
