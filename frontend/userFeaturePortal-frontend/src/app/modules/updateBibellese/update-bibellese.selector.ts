import {createFeatureSelector, createSelector} from "@ngrx/store";
import {UPDATE_BIBELLESE_FEATURE_NAME, UpdateBibelleseState} from "./update-bibellese.state";
import {UpdateBibelleseDto} from "./update-bibellese.dto";

const updateBibelleseState = createFeatureSelector<UpdateBibelleseState>(UPDATE_BIBELLESE_FEATURE_NAME)
export const updateBibellese = createSelector(updateBibelleseState, (state: UpdateBibelleseState): UpdateBibelleseDto => state.bibellese)
