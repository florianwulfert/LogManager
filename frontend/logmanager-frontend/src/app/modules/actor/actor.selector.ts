import {createFeatureSelector, createSelector} from "@ngrx/store";
import {ACTOR_FEATURE_NAME, ActorState} from "./actor.state";
import {ACTOR_IS_VALID_FEATURE_NAME, ActorIsValidState} from "./actorIsValid.state";

const actorState = createFeatureSelector<ActorState>(ACTOR_FEATURE_NAME)
export const getActor = createSelector(actorState, (state: ActorState): string => state.response)

const actorIsValidState = createFeatureSelector<ActorIsValidState>(ACTOR_IS_VALID_FEATURE_NAME)
export const getActorIsValid = createSelector(actorIsValidState, (state: ActorIsValidState): boolean => state.response)
