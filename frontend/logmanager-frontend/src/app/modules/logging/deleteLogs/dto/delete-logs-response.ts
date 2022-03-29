import {LogsDto} from "../../getLogs/dto/logs.dto";

export interface DeleteLogsResponse {
  logsList: LogsDto[],
  returnMessage: string
}

export interface DeleteLogsErrorResponse {
  error: string
}
