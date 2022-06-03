import {UpdateBibelleseDto} from "./update-bibellese.dto";

export const UPDATE_BIBELLESE_FEATURE_NAME = 'update_bibellese'

export interface UpdateBibelleseState {
  bibellese: UpdateBibelleseDto
}

const bibellese: UpdateBibelleseDto = {
  id: "0",
  text: "",
  lieblingsvers: [],
  versText: "",
  label: [],
  leser: "",
  kommentar: "",
}

export const BIBELLESE_INITIAL_STATE: UpdateBibelleseState = {
  bibellese: bibellese,
}
