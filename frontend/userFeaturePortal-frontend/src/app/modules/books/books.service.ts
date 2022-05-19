import {Injectable, OnDestroy} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable, Subject, throwError} from "rxjs";
import {catchError, map, takeUntil} from "rxjs/operators";
import {ActorFacade} from "../actor/actor.facade";
import {FeatureManager} from "../../../assets/utils/feature.manager";
import {GetBooksResponse} from "./getBooks/get-books-response";
import {AddBookRequest} from "./addBooks/add-book-request";
import {AddBookResponse} from "./addBooks/add-book-response";
import {DeleteBookResponse} from "./deleteBook/delete-book-response";
import {DeleteBooksResponse} from "./deleteBooks/delete-books-response";

const API_GET_BOOKS = 'http://localhost:8081/books'
const API_ADD_BOOK = 'http://localhost:8081/book'
const API_UPDATE_BOOK = 'http://localhost:8081/bookUpdate'
const API_DELETE_BOOK = 'http://localhost:8081/book/titel?titel='
const API_ADD_BOOK_TO_USER = 'http://localhost:8081/user/favouriteBook?bookTitel='
const API_DELETE_BOOKS = 'http://localhost:8081/books'

@Injectable({
  providedIn: 'root'
})
export class BooksService implements OnDestroy{
  constructor(private readonly http: HttpClient, private readonly actorFacade: ActorFacade, private featureManager: FeatureManager) {
  }

  ngOnDestroy() {
    this.onDestroy.next(null)
    this.onDestroy.complete()
  }

  name: string | undefined
  onDestroy = new Subject()

  getBooks(): Observable<GetBooksResponse> {
    return this.http.get<GetBooksResponse>(API_GET_BOOKS, {
      observe: 'response'
    }).pipe(
      map((r) => {
        return r.body || {
          result: [],
          returnMessage: ""
        }
      }),
      catchError((err) => {
        if (err.error instanceof Object) {
          this.featureManager.openSnackbar(err.error.text);
        } else {
          this.featureManager.openSnackbar(err.error);
        }
        return throwError('Due to technical issues it is currently not possible to request books.');
      })
    );
  }

  addBook(addBookRequest: AddBookRequest): Observable<AddBookResponse> {
    this.actorFacade.stateActor$.pipe(takeUntil(this.onDestroy)).subscribe(r => {
      this.name = r
    })
    return this.http.post<any>(API_ADD_BOOK, {...addBookRequest, actor: this.name}, {
      observe: 'response'
    }).pipe(
      map((r) => {
        this.featureManager.openSnackbar(r.body?.returnMessage);
        return r.body || {
          result: [],
          returnMessage: ''
        }
      }),
      catchError((err) => {
        if(err.error instanceof Object) {
          this.featureManager.openSnackbar(err.error.text);
        } else {
          this.featureManager.openSnackbar(err.error);
        }
        return throwError('Due to technical issues it is currently not possible to add books.');
      })
    );
  }

  updateBook(updateBookRequest: AddBookRequest): Observable<AddBookResponse> {
    this.actorFacade.stateActor$.pipe(takeUntil(this.onDestroy)).subscribe(r => {
      this.name = r
    })
    return this.http.post<any>(API_UPDATE_BOOK, {...updateBookRequest, actor: this.name}, {
      observe: 'response'
    }).pipe(
      map((r) => {
        this.featureManager.openSnackbar(r.body?.returnMessage);
        return r.body || {
          result: [],
          returnMessage: ''
        }
      }),
      catchError((err) => {
        if(err.error instanceof Object) {
          this.featureManager.openSnackbar(err.error.text);
        } else {
          this.featureManager.openSnackbar(err.error);
        }
        return throwError('Due to technical issues it is currently not possible to update books.');
      })
    );
  }

  deleteBook(titel: string): Observable<DeleteBookResponse> {
    this.actorFacade.stateActor$.pipe(takeUntil(this.onDestroy)).subscribe(r => {
      this.name = r
    })
    return this.http.delete<DeleteBookResponse>(API_DELETE_BOOK + titel + '&actor=' + this.name, {
      observe: 'response'
    }).pipe(
      map((r) => {
        this.featureManager.openSnackbar("Book with the title " + titel + " was deleted.");
        return r.body || {
          result: [],
          returnMessage: ""
        }
      }),
      catchError((err) => {
        if(err.error instanceof Object) {
          this.featureManager.openSnackbar(err.error.text);
        } else {
          this.featureManager.openSnackbar(err.error);
        }
        return throwError('Due to technical issues it is currently not possible to delete this book.')
      })
    );
  }

  deleteBooks(): Observable<DeleteBooksResponse> {
    this.actorFacade.stateActor$.pipe(takeUntil(this.onDestroy)).subscribe(r => {
      this.name = r
    })
    return this.http.delete<DeleteBooksResponse>(API_DELETE_BOOKS + '?actor=' + this.name, {
      observe: 'response'
    }).pipe(
      map((r) => {
        this.featureManager.openSnackbar(r.body?.returnMessage);
        return r.body || {
          result: [],
          returnMessage: ''
        }
      }),
      catchError((err) => {
        if (err.error instanceof Object) {
          this.featureManager.openSnackbar(err.error.text);
        } else {
          this.featureManager.openSnackbar(err.error);
        }
        return throwError('Due to technical issues it is currently not possible to delete all books.');
      })
    );
  }

  assignBookToUser(book: AddBookRequest): Observable<AddBookResponse> {
    this.actorFacade.stateActor$.pipe(takeUntil(this.onDestroy)).subscribe(r => {
      this.name = r
    })
    return this.http.post<AddBookResponse>(API_ADD_BOOK_TO_USER + book.titel + '&actor=' + this.name, {
      observe: 'response'
    }).pipe(
      map((r) => {
        this.featureManager.openSnackbar("Book " + book.titel + " was assigned to user " + this.name + ".");
        return r || {
          result: [],
          returnMessage: ""
        }
      }),
      catchError((err) => {
        if(err.error instanceof Object) {
          this.featureManager.openSnackbar(err.error.text);
        } else {
          this.featureManager.openSnackbar(err.error);
        }
        return throwError('Due to technical issues it is currently not possible to delete this book.')
      })
    );
  }

}
