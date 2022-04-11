import {Injectable} from "@angular/core";
import {Observable, of} from "rxjs";
import {Action} from "@ngrx/store";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {catchError, map, switchMap} from "rxjs/operators";
import {getBooksAction, getBooksResponseAction, loadGetBooksErrorAction} from "./books.actions";
import {BooksService} from "./books.service";


@Injectable({providedIn: 'root'})
export class BooksEffects {
  get$: Observable<Action> = createEffect(() =>
    this.actions$.pipe(
      ofType(getBooksAction),
      switchMap(() =>
        this.booksService.getBooks().pipe(
          map((getBooksResponse) => getBooksResponseAction(getBooksResponse)),
          catchError((error: string) => of(loadGetBooksErrorAction({error})))
        )
      )
    )
  );

  constructor(private readonly actions$: Actions, private readonly booksService: BooksService) {
  }
}
