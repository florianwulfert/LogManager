import {createAction, props} from "@ngrx/store";
import {ActorDto} from "./actor.dto";
import {ActorIsValidErrorResponse, ActorIsValidResponse} from "./actor-valid-response";

export const changeActorAction = createAction('change actor')
export const changeActorResponseAction = createAction('get response on change actor', props<ActorDto>())

export const actorIsValidAction = createAction('validate actor', props<ActorDto>())
export const actorIsValidResponseAction = createAction('get response on validate actor', props<ActorIsValidResponse>())
export const loadActorIsValidErrorAction = createAction('Load validate actor failure', props<ActorIsValidErrorResponse>())
