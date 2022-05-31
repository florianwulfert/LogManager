import {createFeatureSelector, createSelector} from "@ngrx/store";
import {BOOKS_FEATURE_NAME, BibelleseState} from "./bibellese.state";
import {BibelleseDto} from "./bibellese.dto";

const bibelleseState = createFeatureSelector<BibelleseState>(BOOKS_FEATURE_NAME)
export const getBibellese = createSelector(bibelleseState, (state: BibelleseState): BibelleseDto[] => state.bibelleseList)
