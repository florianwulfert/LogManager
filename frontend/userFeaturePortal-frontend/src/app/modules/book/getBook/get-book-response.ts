import {BookDto} from "./book.dto";

export interface GetBookResponse {
  book: BookDto
}

export interface GetBookErrorResponse {
  error: string
}
