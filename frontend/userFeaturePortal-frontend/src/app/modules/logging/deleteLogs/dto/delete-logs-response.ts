import {LogsDto} from "../../getLogs/dto/logs.dto";

export interface DeleteLogsResponse {
  result: LogsDto[],
  returnMessage: string
}

export interface DeleteLogsErrorResponse {
  error: string
}
