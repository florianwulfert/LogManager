import {BibelleseDto} from "./getBibellese/bibellese.dto";

export const BOOKS_FEATURE_NAME = 'books'

export interface BibelleseState {
  booksList: BibelleseDto[]
}

export const BOOKS_GET_INITIAL_STATE: BibelleseState = {
  booksList: [],
}
