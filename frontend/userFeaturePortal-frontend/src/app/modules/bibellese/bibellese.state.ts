import {BibelleseDto} from "./bibellese.dto";

export const BOOKS_FEATURE_NAME = 'books'

export interface BibelleseState {
  bibelleseList: BibelleseDto[]
}

export const BIBELLESE_INITIAL_STATE: BibelleseState = {
  bibelleseList: [],
}
