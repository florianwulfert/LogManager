import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {catchError, map} from "rxjs/operators";
import {SubscriptionManager} from "../../../assets/utils/subscription.manager";
import {ActorFacade} from "../actor/actor.facade";
import {FeatureManager} from "../../../assets/utils/feature.manager";
import {GetBooksResponse} from "./getBooks/get-books-response";
import {AddBookRequest} from "./addBooks/add-book-request";
import {AddBookResponse} from "./addBooks/add-book-response";
import {DeleteBookResponse} from "./deleteBook/delete-book-response";

const API_GET_BOOKS = 'http://localhost:8081/books?actor='
const API_ADD_BOOK = 'http://localhost:8081/book'
const API_DELETE_BOOK = 'http://localhost:8081/deletebookById/'

@Injectable({
  providedIn: 'root'
})
export class BooksService {
  constructor(private readonly http: HttpClient, private readonly actorFacade: ActorFacade, private featureManager: FeatureManager) {
  }

  name: string | undefined
  subscriptionManager = new SubscriptionManager();

  getBooks(): Observable<GetBooksResponse> {
    this.subscriptionManager.add(this.actorFacade.stateActor$).subscribe(r => {
      this.name = r
    })
    return this.http.get<GetBooksResponse>(API_GET_BOOKS + this.name, {
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
    this.subscriptionManager.add(this.actorFacade.stateActor$).subscribe(r => {
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

  deleteBook(i: number | undefined): Observable<DeleteBookResponse> {
    this.subscriptionManager.add(this.actorFacade.stateActor$).subscribe(r => {
      this.name = r
    })
    return this.http.delete<DeleteBookResponse>(API_DELETE_BOOK + i + '?actor=' + this.name, {
      observe: 'response'
    }).pipe(
      map((r) => {
        this.featureManager.openSnackbar("Book with the ID " + i + " was deleted.");
        console.log(r.body)
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
}
