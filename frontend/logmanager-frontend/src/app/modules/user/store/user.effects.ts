import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {Action} from '@ngrx/store';
import {getUserResponseAction, getUsersAction, loadGetUserErrorAction} from 'src/app/modules/user/store/user.actions'
import {Observable, of} from 'rxjs';
import {map, switchMap} from 'rxjs/internal/operators';
import {catchError} from 'rxjs/operators';
import {UserService} from "../service/user.service";
import {GetUserRequest} from "../dto/get-user-request";

@Injectable({ providedIn: 'root' })
export class UserEffects {
  search$: Observable<Action> = createEffect(() =>
    this.actions$.pipe(
      ofType(getUsersAction),
      switchMap((getUserRequest: GetUserRequest) =>
        this.userService.getUsers().pipe(
          map((getUserResponse) => getUserResponseAction(getUserResponse)),
          catchError((error: string) => of(loadGetUserErrorAction({ error })))
        )
      )
    )
  );

  constructor(private readonly actions$: Actions, private readonly userService: UserService) {}
}
