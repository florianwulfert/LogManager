import {BibelleseDto} from "./bibellese.dto";

export const BIBELLESE_FEATURE_NAME = 'bibellese'

export interface BibelleseState {
  bibelleseList: BibelleseDto[]
}

export const BIBELLESE_INITIAL_STATE: BibelleseState = {
  bibelleseList: [],
}
