import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {StoreModule} from "@ngrx/store";
import {EffectsModule} from "@ngrx/effects";
import {BookEffects} from "./book.effects";
import {BookFacade} from "./book.facade";
import {BookReducer} from "./book.reducer";
import {FeatureManager} from "../../../assets/utils/feature.manager";
import {BOOK_FEATURE_NAME} from "./book.state";

@NgModule({
  declarations: [],
  imports: [
    CommonModule, StoreModule.forRoot({}),
    StoreModule.forFeature(BOOK_FEATURE_NAME, BookReducer),
    EffectsModule.forRoot(), EffectsModule.forFeature([BookEffects])],
  providers: [BookFacade, FeatureManager]
})
export class BookModule {
}
