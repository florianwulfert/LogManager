import {createFeatureSelector, createSelector} from "@ngrx/store";
import {BOOK_FEATURE_NAME, BookState} from "./book.state";
import {BookDto} from "./getBook/book.dto";

const bookGetState = createFeatureSelector<BookState>(BOOK_FEATURE_NAME);
export const getBook = createSelector(bookGetState, (state: BookState): BookDto => state.book);
