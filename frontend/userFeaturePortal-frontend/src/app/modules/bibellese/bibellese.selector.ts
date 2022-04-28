import {createFeatureSelector, createSelector} from "@ngrx/store";
import {BOOKS_FEATURE_NAME, BibelleseState} from "./bibellese.state";
import {BibelleseDto} from "./getBibellese/bibellese.dto";

const booksGetState = createFeatureSelector<BibelleseState>(BOOKS_FEATURE_NAME)
export const getBooks = createSelector(booksGetState, (state: BibelleseState): BibelleseDto[] => state.booksList)
