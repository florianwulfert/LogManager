import {BooksDto} from "../getBooks/books.dto";

export interface AddBookResponse {
  result: BooksDto[]
  returnMessage: string
}

export interface AddBookErrorResponse {
  error: string
}
