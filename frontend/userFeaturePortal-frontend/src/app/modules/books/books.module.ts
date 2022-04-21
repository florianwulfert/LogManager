import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {StoreModule} from "@ngrx/store";
import {EffectsModule} from "@ngrx/effects";
import {BooksEffects} from "./books.effects";
import {BooksFacade} from "./books.facade";
import {BooksReducer} from "./books.reducer";
import {FeatureManager} from "../../../assets/utils/feature.manager";
import {BOOKS_FEATURE_NAME} from "./books.state";

@NgModule({
  declarations: [],
  imports: [
    CommonModule, StoreModule.forRoot({}),
    StoreModule.forFeature(BOOKS_FEATURE_NAME, BooksReducer),
    EffectsModule.forRoot(), EffectsModule.forFeature([BooksEffects])],
  providers: [BooksFacade, FeatureManager]
})
export class BooksModule {
}
