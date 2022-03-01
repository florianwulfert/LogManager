import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {Action} from '@ngrx/store';
import {
  addUserAction,
  addUserResponseAction,
  deleteUserAction,
  deleteUserResponseAction,
  getUserResponseAction,
  getUsersAction,
  loadAddUserErrorAction,
  loadDeleteUserErrorAction,
  loadGetUserErrorAction
} from 'src/app/modules/user/user.actions'
import {Observable, of} from 'rxjs';
import {map, switchMap} from 'rxjs/internal/operators';
import {catchError} from 'rxjs/operators';
import {UserService} from "./user.service";
import {AddUserRequest} from "./addUser/dto/add-user-request";

@Injectable({ providedIn: 'root' })
export class UserEffects {
  get$: Observable<Action> = createEffect(() =>
    this.actions$.pipe(
      ofType(getUsersAction),
      switchMap(() =>
        this.userService.getUsers().pipe(
          map((getUserResponse) => getUserResponseAction(getUserResponse)),
          catchError((error: string) => of(loadGetUserErrorAction({ error })))
        )
      )
    )
  );

  add$: Observable<Action> = createEffect(() =>
    this.actions$.pipe(
      ofType(addUserAction),
      switchMap((addUserRequest:AddUserRequest) =>
        this.userService.addUser(addUserRequest).pipe(
          map((addUserResponse) => addUserResponseAction(addUserResponse)),
          catchError((error: string) => of(loadAddUserErrorAction({ error })))
        )
      )
    )
  );

  delete$: Observable<Action> = createEffect(() =>
    this.actions$.pipe(
      ofType(deleteUserAction),
      switchMap(() =>
        this.userService.deleteUser().pipe(
          map((deleteUserResponse) => deleteUserResponseAction(deleteUserResponse)),
          catchError((error: string) => of(loadDeleteUserErrorAction({ error })))
        )
      )
    )
  );

  constructor(private readonly actions$: Actions, private readonly userService: UserService) {}
}