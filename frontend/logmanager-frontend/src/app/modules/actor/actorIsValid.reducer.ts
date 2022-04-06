import {createReducer, on} from "@ngrx/store";
import {actorIsValidAction, actorIsValidResponseAction} from "./actor.action";
import {ACTOR_IS_VALID_INITIAL_STATE, ActorIsValidState} from "./actorIsValid.state";
import {ActorIsValidResponse} from "./actor-valid-response";

const handleActorIsValidResponse = (state: ActorIsValidState, resp: ActorIsValidResponse): ActorIsValidState => {
  return {
    ...state,
    response: resp.foundUser,
  };
}

const handleActorIsValid = (state: ActorIsValidState): ActorIsValidState => {
  return {
    ...state,
  };
};

export const ActorIsValidReducer = createReducer(
  ACTOR_IS_VALID_INITIAL_STATE,
  on(actorIsValidAction, handleActorIsValid),
  on(actorIsValidResponseAction, handleActorIsValidResponse)

)
