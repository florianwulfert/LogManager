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
  loadGetUsersErrorAction,
  loadUpdateUserErrorAction,
  updateUserAction,
  updateUserResponseAction
} from 'src/app/modules/users/users.actions'
import {Observable, of} from 'rxjs';
import {catchError, map, switchMap} from 'rxjs/operators';
import {UsersService} from "./users.service";
import {AddUserRequest} from "./addUser/add-user-request";
import {DeleteUserRequest} from "./deleteUser/delete-user-request";

@Injectable({providedIn: 'root'})
export class UsersEffects {
  getUsers$: Observable<Action> = createEffect(() =>
    this.actions$.pipe(
      ofType(getUsersAction),
      switchMap(() =>
        this.usersService.getUsers().pipe(
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
        this.usersService.addUser(addUserRequest).pipe(
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
        this.usersService.deleteUsers().pipe(
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
        this.usersService.deleteUser(deleteUserRequest.name).pipe(
          map((deleteUserResponse) =>
            deleteUserResponseAction(deleteUserResponse)),
          catchError((error: string) => of(loadDeleteUserErrorAction({error})))
        )
      )
    )
  );

  update$: Observable<Action> = createEffect(() =>
    this.actions$.pipe(
      ofType(updateUserAction),
      switchMap((updateUserRequest: AddUserRequest) =>
        this.usersService.updateUser(updateUserRequest).pipe(
          map((addUserResponse) => updateUserResponseAction(addUserResponse)),
          catchError((error: string) => of(loadUpdateUserErrorAction({error})))
        )
      )
    )
  );

  constructor(private readonly actions$: Actions, private readonly usersService: UsersService) {
  }
}
