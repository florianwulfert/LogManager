import {actorIsValidAction, changeActorResponseAction} from "./actor.action";
import {Injectable} from "@angular/core";
import {Store} from "@ngrx/store";
import {ActorState} from "./actor.state";
import {ActorDto} from "./actor.dto";
import {getActor, getActorIsValid} from "./actor.selector";
import {ActorIsValidState} from "./actorIsValid.state";

@Injectable({providedIn: 'root'})
export class ActorFacade {

  stateActor$ = this.actorState.select(getActor)
  stateActorIsValid$ = this.actorIsValidState.select(getActorIsValid)

  constructor(
    private readonly actorState: Store<ActorState>,
    private readonly actorIsValidState: Store<ActorIsValidState>
  ) {
  }

  saveActor(actorDto: ActorDto): void {
    this.actorState.dispatch(changeActorResponseAction(actorDto))
  }

  getUserByName(actorDto: ActorDto): void {
    this.actorIsValidState.dispatch(actorIsValidAction(actorDto));
  }
}
