import {BooksDto} from "./getBooks/books.dto";

export const BOOKS_FEATURE_NAME = 'books'

export interface BooksState {
  booksList: BooksDto[]
}

export const BOOKS_GET_INITIAL_STATE: BooksState = {
  booksList: [],
}
