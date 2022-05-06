import {BibelleseDto} from "../bibellese.dto";

export interface GetBibelleseResponse {
  result: BibelleseDto[]
}

export interface GetBibelleseErrorResponse {
  error: string
}
