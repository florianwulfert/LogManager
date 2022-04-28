import {BooksDto} from "./books.dto";

export interface GetBooksResponse {
  result: BooksDto[]
  returnMessage: string
}

export interface GetBooksErrorResponse {
  error: string
}
