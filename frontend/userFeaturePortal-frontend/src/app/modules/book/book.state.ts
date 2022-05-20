import {BookDto} from "./getBook/book.dto";

export const BOOK_FEATURE_NAME = 'book';

export interface BookState {
  book: BookDto
}

const book: BookDto = {
  titel: "",
  erscheinungsjahr: 0
}

export const BOOK_GET_INITIAL_STATE: BookState = {
  book: book
}
