import {LogsDto} from "./getLogs/dto/logs.dto";

export const LOGS_FEATURE_NAME = 'logs'

export interface LogsState {
  logsList: LogsDto[]
  error: string
}

export const LOGS_GET_INITIAL_STATE: LogsState = {
  logsList: [],
  error: ''
}
