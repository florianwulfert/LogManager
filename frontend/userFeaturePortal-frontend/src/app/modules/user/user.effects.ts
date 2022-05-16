import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {Action} from '@ngrx/store';
import {Observable, of} from 'rxjs';
import {catchError, map, switchMap} from 'rxjs/operators';
import {UserService} from "./user.service";
import {getUserAction, getUserResponseAction, loadGetUserErrorAction} from "./user.actions";

@Injectable({providedIn: 'root'})
export class UserEffects {
  getUser$: Observable<Action> = createEffect(() =>
      this.actions$.pipe(
        ofType(getUserAction),
        switchMap(() =>
          this.userService.getUser().pipe(
            map((getUserResponse) => getUserResponseAction(getUserResponse)),
            catchError((error: string) => of(loadGetUserErrorAction({error})))
          )
        )
      )
  );

  constructor(private readonly actions$: Actions, private readonly userService: UserService) {
  }
}
