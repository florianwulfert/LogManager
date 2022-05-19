import {Injectable} from "@angular/core";
import {Store} from "@ngrx/store";
import {getBook} from "./book.selector";
import {BookState} from "./book.state";
import {GetBookRequest} from "./getBook/get-book-request";
import {getBookAction} from "./book.actions";

@Injectable({providedIn: 'root'})
export class BookFacade {
  stateGetBookResponse$ = this.bookState.select(getBook)

  constructor(
    private readonly bookState: Store<BookState>,
  ) {}

  getBook(request: GetBookRequest): void {
    this.bookState.dispatch(getBookAction(request))
  }
}
