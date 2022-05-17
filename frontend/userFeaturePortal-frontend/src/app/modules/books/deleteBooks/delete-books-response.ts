import {BooksDto} from "../getBooks/books.dto";

export interface DeleteBooksResponse {
  result: BooksDto[],
  returnMessage: string
}

export interface DeleteBooksErrorResponse {
  error: string
}
