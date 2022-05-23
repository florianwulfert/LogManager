import {Injectable} from "@angular/core";
import {Observable, of} from "rxjs";
import {Action} from "@ngrx/store";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {catchError, map, switchMap} from "rxjs/operators";
import {
  deleteFavouriteBookAction,
  deleteFavouriteBookResponseAction,
  getFavouriteBookAction,
  getFavouriteBookResponseAction,
  loadDeleteFavouriteBookErrorAction,
  loadGetFavouriteBookErrorAction
} from "./favouriteBook.actions";
import {FavouriteBookService} from "./favouriteBook.service";

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

  constructor(private readonly actions$: Actions, private readonly favouriteBookService: FavouriteBookService) {
  }
}
