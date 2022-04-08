import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {StoreModule} from "@ngrx/store";
import {ActorFacade} from "./actor.facade";
import {EffectsModule} from "@ngrx/effects";
import {ACTOR_IS_VALID_FEATURE_NAME} from "./actorIsValid.state";
import {ActorIsValidReducer} from "./actorIsValid.reducer";
import {ActorEffects} from "./actor.effects";
import {ActorService} from "./actor.service";

@NgModule({
  declarations: [],
  imports: [
    CommonModule, EffectsModule.forRoot(), StoreModule.forRoot({}),
    StoreModule.forFeature(ACTOR_IS_VALID_FEATURE_NAME, ActorIsValidReducer),
    EffectsModule.forRoot(), EffectsModule.forFeature([ActorEffects])],
  providers: [ActorFacade, ActorService]
})

export class ActorIsValidModule {
}
