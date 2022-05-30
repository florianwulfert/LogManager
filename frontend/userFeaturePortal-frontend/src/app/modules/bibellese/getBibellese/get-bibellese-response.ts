import {BibelleseDto} from "../bibellese.dto";

export interface GetBibelleseResponse {
  result: BibelleseDto[]
  returnMessage: string
}

export interface GetBibelleseErrorResponse {
  error: string
}
