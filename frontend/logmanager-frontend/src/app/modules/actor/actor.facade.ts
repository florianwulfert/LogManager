import {changeActorResponseAction} from "./actor.action";
import {Injectable} from "@angular/core";
import {Store} from "@ngrx/store";
import {ActorState} from "./actor.state";
import {ActorDto} from "./actor.dto";
import {getActor} from "./actor.selector";

@Injectable({providedIn: 'root'})
export class ActorFacade {

  stateActor$ = this.actorState.select(getActor)

  constructor(
    private readonly actorState: Store<ActorState>
  ) {
  }

  saveActor(actorDto: ActorDto): void {
    this.actorState.dispatch(changeActorResponseAction(actorDto))
  }
}
