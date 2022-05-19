import {Injectable} from "@angular/core";
import {Observable, of} from "rxjs";
import {Action} from "@ngrx/store";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {catchError, map, switchMap} from "rxjs/operators";
import {
  addBookAction,
  addBookResponseAction,
  assignBookToUserAction,
  assignBookToUserResponseAction,
  deleteBookAction,
  deleteBookResponseAction,
  deleteBooksAction,
  deleteBooksResponseAction,
  deleteFavouriteBookAction,
  deleteFavouriteBookResponseAction,
  getBooksAction,
  getBooksResponseAction,
  loadAddBookErrorAction,
  loadAssignBookToUserErrorAction,
  loadDeleteBookErrorAction,
  loadDeleteBooksErrorAction,
  loadDeleteFavouriteBookErrorAction,
  loadGetBooksErrorAction
} from "./books.actions";
import {BooksService} from "./books.service";
import {AddBookRequest} from "./addBooks/add-book-request";
import {DeleteBookRequest} from "./deleteBook/delete-book-request";


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

  add$: Observable<Action> = createEffect(() =>
    this.actions$.pipe(
      ofType(addBookAction),
      switchMap((addBookRequest: AddBookRequest) =>
        this.booksService.addBook(addBookRequest).pipe(
          map((addBookResponse) => addBookResponseAction(addBookResponse)),
          catchError((error: string) => of(loadAddBookErrorAction({error})))
        )
      )
    )
  );

  deleteBook$: Observable<Action> = createEffect(() =>
    this.actions$.pipe(
      ofType(deleteBookAction),
      switchMap((deleteBookRequest: DeleteBookRequest) =>
        this.booksService.deleteBook(deleteBookRequest.titel).pipe(
          map((deleteBookResponse) =>
            deleteBookResponseAction(deleteBookResponse)),
          catchError((error: string) => of(loadDeleteBookErrorAction({error})))
        )
      )
    )
  );

  deleteBooks$: Observable<Action> = createEffect(() =>
    this.actions$.pipe(
      ofType(deleteBooksAction),
      switchMap(() =>
        this.booksService.deleteBooks().pipe(
          map((deleteBooksResponse) => deleteBooksResponseAction(deleteBooksResponse)),
          catchError((error: string) => of(loadDeleteBooksErrorAction({error})))
        )
      )
    )
  );

  assignBookToUser$: Observable<Action> = createEffect(() =>
    this.actions$.pipe(
      ofType(assignBookToUserAction),
      switchMap((assignBookToUserRequest: AddBookRequest) =>
        this.booksService.assignBookToUser(assignBookToUserRequest).pipe(
          map((addBookResponse) =>
            assignBookToUserResponseAction(addBookResponse)),
          catchError((error: string) => of(loadAssignBookToUserErrorAction({error})))
        )
      )
    )
  );

  deleteFavouriteBook$: Observable<Action> = createEffect(() =>
    this.actions$.pipe(
      ofType(deleteFavouriteBookAction),
      switchMap(() =>
        this.booksService.deleteFavouriteBook().pipe(
          map((deleteFavouriteBookResponse) =>
            deleteFavouriteBookResponseAction(deleteFavouriteBookResponse)),
          catchError((error: string) => of(loadDeleteFavouriteBookErrorAction({error})))
        )
      )
    )
  );

  constructor(private readonly actions$: Actions, private readonly booksService: BooksService) {
  }
}
