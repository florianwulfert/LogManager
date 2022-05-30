import {Injectable} from "@angular/core";
import {Observable, of} from "rxjs";
import {Action} from "@ngrx/store";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {catchError, map, switchMap} from "rxjs/operators";
import {
  assignBookToUserAction,
  assignBookToUserResponseAction,
  deleteFavouriteBookAction,
  deleteFavouriteBookResponseAction,
  getFavouriteBookAction,
  getFavouriteBookResponseAction,
  loadAssignBookToUserErrorAction,
  loadDeleteFavouriteBookErrorAction,
  loadGetFavouriteBookErrorAction
} from "./favouriteBook.actions";
import {FavouriteBookService} from "./favouriteBook.service";
import {AddBookRequest} from "../books/addBooks/add-book-request";

@Injectable({providedIn: 'root'})
export class FavouriteBookEffects {

  deleteFavouriteBook$: Observable<Action> = createEffect(() =>
    this.actions$.pipe(
      ofType(deleteFavouriteBookAction),
      switchMap(() =>
        this.favouriteBookService.deleteFavouriteBook().pipe(
          map((deleteFavouriteBookResponse) =>
            deleteFavouriteBookResponseAction(deleteFavouriteBookResponse)),
          catchError((error: string) => of(loadDeleteFavouriteBookErrorAction({error})))
        )
      )
    )
  );

  getFavouriteBook$: Observable<Action> = createEffect(() =>
    this.actions$.pipe(
      ofType(getFavouriteBookAction),
      switchMap(() =>
        this.favouriteBookService.getFavouriteBook().pipe(
          map((getFavouriteBookResponse) => getFavouriteBookResponseAction(getFavouriteBookResponse)),
          catchError((error: string) => of(loadGetFavouriteBookErrorAction({error})))
        )
      )
    )
  );

  assignBookToUser$: Observable<Action> = createEffect(() =>
    this.actions$.pipe(
      ofType(assignBookToUserAction),
      switchMap((assignBookToUserRequest: AddBookRequest) =>
        this.favouriteBookService.assignBookToUser(assignBookToUserRequest).pipe(
          map((addBookResponse) =>
            assignBookToUserResponseAction(addBookResponse)),
          catchError((error: string) => of(loadAssignBookToUserErrorAction({error})))
        )
      )
    )
  );


  constructor(private readonly actions$: Actions, private readonly favouriteBookService: FavouriteBookService) {
  }
}
