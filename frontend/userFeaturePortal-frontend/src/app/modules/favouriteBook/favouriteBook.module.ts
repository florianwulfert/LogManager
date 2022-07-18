import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {StoreModule} from "@ngrx/store";
import {EffectsModule} from "@ngrx/effects";
import {FavouriteBookEffects} from "./favouriteBook.effects";
import {FavouriteBookFacade} from "./favouriteBook.facade";
import {FavouriteBookReducer} from "./favouriteBook.reducer";
import {FeatureManager} from "../../../assets/utils/feature.manager";
import {FAVOURITE_BOOK_FEATURE_NAME} from "./favouriteBook.state";

@NgModule({
  declarations: [],
  imports: [
    CommonModule, StoreModule.forRoot({}),
    StoreModule.forFeature(FAVOURITE_BOOK_FEATURE_NAME, FavouriteBookReducer),
    EffectsModule.forRoot(), EffectsModule.forFeature([FavouriteBookEffects])],
  providers: [FavouriteBookFacade, FeatureManager]
})
export class FavouriteBookModule {
}
