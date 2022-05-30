import {Injectable, OnDestroy} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable, Subject, throwError} from "rxjs";
import {catchError, map, takeUntil} from "rxjs/operators";
import {ActorFacade} from "../actor/actor.facade";
import {FeatureManager} from "../../../assets/utils/feature.manager";
import {FavouriteBookResponse} from "./getFavouriteBook/favouriteBook-response";
import {AddBookRequest} from "../books/addBooks/add-book-request";

const API_DELETE_FAVOURITE_BOOK = 'http://localhost:8081/user/favouriteBook/delete?name='
const API_GET_FAVOURITE_BOOK = 'http://localhost:8081/user/favouriteBook?name='
const API_ADD_BOOK_TO_USER = 'http://localhost:8081/user/favouriteBook?bookTitel='


@Injectable({
  providedIn: 'root'
})
export class FavouriteBookService implements OnDestroy{
  constructor(private readonly http: HttpClient, private readonly actorFacade: ActorFacade, private featureManager: FeatureManager) {
  }

  ngOnDestroy() {
    this.onDestroy.next(null)
    this.onDestroy.complete()
  }

  name: string | undefined
  onDestroy = new Subject()

  deleteFavouriteBook(): Observable<FavouriteBookResponse> {
    this.actorFacade.stateActor$.pipe(takeUntil(this.onDestroy)).subscribe(r => {
      this.name = r
    })
    return this.http.post<FavouriteBookResponse>(API_DELETE_FAVOURITE_BOOK + this.name, {
      observe: 'response'
    }).pipe(
      map((r) => {
        this.featureManager.openSnackbar(r.returnMessage)
        return r || {
          favouriteBook: "",
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

  getFavouriteBook(): Observable<FavouriteBookResponse> {
    this.actorFacade.stateActor$.pipe(takeUntil(this.onDestroy)).subscribe(r => {
      this.name = r
    })
    return this.http.get<FavouriteBookResponse>(API_GET_FAVOURITE_BOOK + this.name, {
      observe: 'response'
    }).pipe(
      map((r) => {
        return r.body || {
          favouriteBook: '',
          returnMessage: ''
        }
      }),
      catchError((err) => {
        if (err.error instanceof Object) {
          this.featureManager.openSnackbar(err.error.text);
          return throwError('Wrong object in interface')
        } if (err.error) {
          this.featureManager.openSnackbar(err.error);
          return throwError('business error')
        }
        this.featureManager.openSnackbar('Due to technical issues it is currently not possible to request favourite book.')
        return throwError('Due to technical issues it is currently not possible to request favourite book.')
      })
    );
  }

  assignBookToUser(book: AddBookRequest): Observable<FavouriteBookResponse> {
    this.actorFacade.stateActor$.pipe(takeUntil(this.onDestroy)).subscribe(r => {
      this.name = r
    })
    return this.http.post<FavouriteBookResponse>(API_ADD_BOOK_TO_USER + book.titel + '&actor=' + this.name, {
      observe: 'response'
    }).pipe(
      map((r) => {
        this.featureManager.openSnackbar(r.returnMessage);
        return r || {
          favouriteBook: "",
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
