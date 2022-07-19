import {BibelleseDto} from "../bibellese.dto";

export interface AddBibelleseResponse {
  result: BibelleseDto[]
  returnMessage: string
}

export interface AddBibelleseErrorResponse {
  error: string
}
