import {LogsDto} from "../../getLogs/dto/logs.dto";

export interface AddLogResponse {
  logsList: LogsDto[]
  returnMessage: string
}

export interface AddLogErrorResponse {
  error: string
}
