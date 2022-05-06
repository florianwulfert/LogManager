import {BibelleseDto} from "../bibellese.dto";

export interface DeleteBibelleseResponse {
  result: BibelleseDto[]
}

export interface DeleteBibelleseErrorResponse {
  error: string
}
