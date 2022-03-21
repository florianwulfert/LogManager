import {createAction, props} from "@ngrx/store";
import {ActorDto} from "./actor.dto";

export const changeActorAction = createAction('change actor')
export const changeActorResponseAction = createAction('get response on change actor', props<ActorDto>())
