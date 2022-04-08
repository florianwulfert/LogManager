import {createReducer, on} from "@ngrx/store";
import {changeActorAction, changeActorResponseAction} from "./actor.action";
import {ACTOR_INITIAL_STATE, ActorState} from "./actor.state";
import {ActorDto} from "./actor.dto";

const handleActorResponse = (state: ActorState, resp: ActorDto): ActorState => {
  return {
    ...state,
    response: resp.actor,
  };
}

const handleActor = (state: ActorState): ActorState => {
  return {
    ...state,
  };
};

export const ActorReducer = createReducer(
  ACTOR_INITIAL_STATE,
  on(changeActorResponseAction, handleActorResponse),
  on(changeActorAction, handleActor)
)
