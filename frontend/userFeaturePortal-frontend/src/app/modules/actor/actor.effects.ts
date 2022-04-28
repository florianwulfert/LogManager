import {Injectable} from "@angular/core";
import {Observable, of} from "rxjs";
import {Action} from "@ngrx/store";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {catchError, map, switchMap} from "rxjs/operators";
import {ActorService} from "./actor.service";
import {actorIsValidAction, actorIsValidResponseAction, loadActorIsValidErrorAction} from "./actor.action";
import {ActorDto} from "./actor.dto";

@Injectable({providedIn: 'root'})
export class ActorEffects {
  get$: Observable<Action> = createEffect(() =>
    this.actions$.pipe(
      ofType(actorIsValidAction),
      switchMap((actorDto: ActorDto) =>
        this.actorService.getUserByName(actorDto.actor).pipe(
          map((findUserResponse) => actorIsValidResponseAction(findUserResponse)),
          catchError((error: string) => of(loadActorIsValidErrorAction({error})))
        )
      )
    )
  );

  constructor(private readonly actions$: Actions, private readonly actorService: ActorService) {
  }
}
