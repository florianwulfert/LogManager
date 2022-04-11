import {Injectable} from "@angular/core";
import {Store} from "@ngrx/store";
import {BooksState} from "./books.state";
import {getBooksAction} from "./books.actions";
import {getBooks} from "./books.selector";

@Injectable({providedIn: 'root'})
export class BooksFacade {
  stateGetBooksResponse$ = this.booksState.select(getBooks)

  constructor(
    private readonly booksState: Store<BooksState>
  ) {
  }

  getBooks(): void {
    this.booksState.dispatch(getBooksAction())
  }

}
