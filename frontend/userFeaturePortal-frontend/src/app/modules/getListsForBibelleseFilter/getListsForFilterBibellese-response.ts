import {GetListsDto} from "./getLists.dto";

export interface GetListsForFilterBibelleseResponse {
  result: GetListsDto[]
}

export interface GetListsForFilterBibelleseErrorResponse {
  error: string
}
