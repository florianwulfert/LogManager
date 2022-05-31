import {BibelleseDto} from "../bibellese.dto";

export interface DeleteBibelleseResponse {
  result: BibelleseDto[]
  returnMessage: string
}

export interface DeleteBibelleseErrorResponse {
  error: string
}
