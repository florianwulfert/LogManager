import {HttpClient} from '@angular/common/http';
import {Injectable, OnDestroy} from '@angular/core';
import {Observable, Subject, throwError} from 'rxjs';
import {catchError, map} from "rxjs/operators";
import {ActorFacade} from "../actor/actor.facade";
import {FeatureManager} from "../../../assets/utils/feature.manager";
import {GetBookRequest} from "./getBook/get-book-request";
import {GetBookResponse} from "./getBook/get-book-response";
import {BookDto} from "./getBook/book.dto";

const API_GET_BOOK = 'http://localhost:8081/book?titel=';

@Injectable({
  providedIn: 'root'
})
export class BookService implements OnDestroy {
  constructor(private readonly http: HttpClient, private readonly actorFacade: ActorFacade, private featureManager: FeatureManager) {
  }

  ngOnDestroy() {
    this.onDestroy.next(null)
    this.onDestroy.complete()
  }

  onDestroy = new Subject()
  bookDto: BookDto = {
    titel: "",
    erscheinungsjahr: 0
  }

  getBook(request: GetBookRequest): Observable<GetBookResponse> {
    return this.http.get<GetBookResponse>(API_GET_BOOK + request.titel, {
      observe: 'response'
    }).pipe(
      map((r) => {
        return r.body || {
          book: this.bookDto
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
        this.featureManager.openSnackbar('Due to technical issues it is currently not possible to request this book.')
        return throwError('Due to technical issues it is currently not possible to request this book.')
      })
    );
  }
}
