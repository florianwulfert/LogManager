import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {Action} from '@ngrx/store';
import {
  addUserAction,
  addUserResponseAction,
  deleteUserAction,
  deleteUserResponseAction,
  deleteUsersAction,
  deleteUsersResponseAction,
  getUsersAction,
  getUsersResponseAction,
  loadAddUserErrorAction,
  loadDeleteUserErrorAction,
  loadDeleteUsersErrorAction,
  loadGetUsersErrorAction
} from 'src/app/modules/user/user.actions'
import {Observable, of} from 'rxjs';
import {catchError, map, switchMap} from 'rxjs/operators';
import {UserService} from "./user.service";
import {AddUserRequest} from "./addUser/add-user-request";
import {DeleteUserRequest} from "./deleteUser/delete-user-request";

@Injectable({providedIn: 'root'})
export class UserEffects {
  getUsers$: Observable<Action> = createEffect(() =>
    this.actions$.pipe(
      ofType(getUsersAction),
      switchMap(() =>
        this.userService.getUsers().pipe(
          map((getUsersResponse) => getUsersResponseAction(getUsersResponse)),
          catchError((error: string) => of(loadGetUsersErrorAction({error})))
        )
      )
    )
  );

  add$: Observable<Action> = createEffect(() =>
    this.actions$.pipe(
      ofType(addUserAction),
      switchMap((addUserRequest: AddUserRequest) =>
        this.userService.addUser(addUserRequest).pipe(
          map((addUserResponse) => addUserResponseAction(addUserResponse)),
          catchError((error: string) => of(loadAddUserErrorAction({error})))
        )
      )
    )
  );

  deleteUsers$: Observable<Action> = createEffect(() =>
    this.actions$.pipe(
      ofType(deleteUsersAction),
      switchMap(() =>
        this.userService.deleteUsers().pipe(
          map((deleteUsersResponse) => deleteUsersResponseAction(deleteUsersResponse)),
          catchError((error: string) => of(loadDeleteUsersErrorAction({error})))
        )
      )
    )
  );

  deleteUser$: Observable<Action> = createEffect(() =>
    this.actions$.pipe(
      ofType(deleteUserAction),
      switchMap((deleteUserRequest: DeleteUserRequest) =>
        this.userService.deleteUser(deleteUserRequest.name).pipe(
          map((deleteUserResponse) =>
            deleteUserResponseAction(deleteUserResponse)),
          catchError((error: string) => of(loadDeleteUserErrorAction({error})))
        )
      )
    )
  );

  constructor(private readonly actions$: Actions, private readonly userService: UserService) {
  }
}
