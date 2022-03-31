import {LogsDto} from "./logs.dto";

export interface GetLogsResponse {
  logsList: LogsDto[]
  returnMessage: string
}

export interface GetLogsErrorResponse {
  error: string
}
