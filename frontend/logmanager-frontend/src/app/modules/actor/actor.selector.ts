import {createFeatureSelector, createSelector} from "@ngrx/store";
import {ACTOR_FEATURE_NAME, ActorState} from "./actor.state";

const actorState = createFeatureSelector<ActorState>(ACTOR_FEATURE_NAME)
export const getActor = createSelector(actorState, (state: ActorState): string => state.response)
