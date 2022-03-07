import {LogsDto} from "./logs.dto";


export interface GetLogsResponse {
  result: LogsDto[]
}

export interface GetLogsErrorResponse {
  error: string
}
