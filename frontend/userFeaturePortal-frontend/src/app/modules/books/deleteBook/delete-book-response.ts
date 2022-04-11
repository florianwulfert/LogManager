import {BooksDto} from "../getBooks/books.dto";

export interface DeleteBookResponse {
  result: BooksDto[]
  returnMessage: string;
}

export interface DeleteBookErrorResponse {
  error: string;
}
