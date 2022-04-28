import {Injectable} from "@angular/core";
import {Store} from "@ngrx/store";
import {BibelleseState} from "./bibellese.state";
import {addBookAction, assignBookToUserAction, deleteBookAction, getBooksAction} from "./bibellese.actions";
import {getBooks} from "./bibellese.selector";
import {AddBookRequest} from "./addBooks/add-book-request";
import {DeleteBookRequest} from "./deleteBook/delete-book-request";

@Injectable({providedIn: 'root'})
export class BibelleseFacade {
  stateGetBooksResponse$ = this.booksState.select(getBooks)

  constructor(
    private readonly booksState: Store<BibelleseState>
  ) {
  }

  getBooks(): void {
    this.booksState.dispatch(getBooksAction())
  }

  addBook(request: AddBookRequest): void {
    this.booksState.dispatch(addBookAction(request));
  }

  deleteBook(request: DeleteBookRequest): void {
    this.booksState.dispatch(deleteBookAction(request))
  }

  assignBookToUser(request: AddBookRequest): void {
    console.log(request)
    this.booksState.dispatch(assignBookToUserAction(request))
  }
}
