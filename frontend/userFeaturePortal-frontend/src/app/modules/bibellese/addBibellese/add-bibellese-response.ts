import {BibelleseDto} from "../bibellese.dto";

export interface AddBibelleseResponse {
  result: BibelleseDto[]
}

export interface AddBibelleseErrorResponse {
  error: string
}
