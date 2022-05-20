import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {Action} from '@ngrx/store';
import {Observable, of} from 'rxjs';
import {catchError, map, switchMap} from 'rxjs/operators';
import {BookService} from "./book.service";
import {getBookAction, getBookResponseAction, loadGetBookErrorAction,} from "./book.actions";
import {GetBookRequest} from "./getBook/get-book-request";

@Injectable({providedIn: 'root'})
export class BookEffects {
  getBook$: Observable<Action> = createEffect(() =>
    this.actions$.pipe(
      ofType(getBookAction),
      switchMap((request: GetBookRequest) =>
        this.bookService.getBook(request).pipe(
          map((getBookResponse) => getBookResponseAction(getBookResponse)),
          catchError((error: string) => of(loadGetBookErrorAction({error})))
        )
      )
    )
  );

  constructor(private readonly actions$: Actions, private readonly bookService: BookService) {
  }
}
