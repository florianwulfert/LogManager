import {LogsDto} from "../../getLogs/dto/logs.dto";

export interface AddLogResponse {
  result: LogsDto[]
  returnMessage: string
}

export interface AddLogErrorResponse {
  error: string
}
