import {LogsDto} from "./logs.dto";

export interface GetLogsResponse {
  result: LogsDto[]
  returnMessage: string
}

export interface GetLogsErrorResponse {
  error: string
}
