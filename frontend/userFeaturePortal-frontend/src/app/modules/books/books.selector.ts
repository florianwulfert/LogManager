import {createFeatureSelector, createSelector} from "@ngrx/store";
import {BOOKS_FEATURE_NAME, BooksState} from "./books.state";
import {BooksDto} from "./getBooks/books.dto";

const booksGetState = createFeatureSelector<BooksState>(BOOKS_FEATURE_NAME)
export const getBooks = createSelector(booksGetState, (state: BooksState): BooksDto[] => state.booksList)
