import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {StoreModule} from "@ngrx/store";
import {ACTOR_FEATURE_NAME} from "./actor.state";
import {ActorFacade} from "./actor.facade";
import {ActorReducer} from "./actor.reducer";

@NgModule({
  declarations: [],
  imports: [
    CommonModule, StoreModule.forRoot({}),
    StoreModule.forFeature(ACTOR_FEATURE_NAME, ActorReducer),
  ],
  providers: [ActorFacade]
})
export class UserModule {
}
