import {Injectable} from "@angular/core";
import {Store} from "@ngrx/store";
import {BooksState} from "./books.state";
import {addBookAction, deleteBookAction, deleteBooksAction, getBooksAction, updateBookAction} from "./books.actions";
import {getBooks} from "./books.selector";
import {AddBookRequest} from "./addBooks/add-book-request";
import {DeleteBookRequest} from "./deleteBook/delete-book-request";

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

  addBook(request: AddBookRequest): void {
    this.booksState.dispatch(addBookAction(request));
  }

  deleteBook(request: DeleteBookRequest): void {
    this.booksState.dispatch(deleteBookAction(request))
  }

  deleteBooks() {
    this.booksState.dispatch(deleteBooksAction())
  }

  updateBook(request: AddBookRequest) {
    this.booksState.dispatch(updateBookAction(request))
  }
}
