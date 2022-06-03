import {UpdateBibelleseDto} from "./update-bibellese.dto";

export interface UpdateBibelleseResponse {
  result: UpdateBibelleseDto
  returnMessage: string
}

export interface UpdateBibelleseErrorResponse {
  error: string
}
