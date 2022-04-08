import {LogsDto} from "../../getLogs/dto/logs.dto";

export interface DeleteLogResponse {
  result: LogsDto[]
  returnMessage: string;
}

export interface DeleteLogErrorResponse {
  error: string;
}
