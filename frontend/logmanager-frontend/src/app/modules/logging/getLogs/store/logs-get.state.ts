import {LogsDto} from "../dto/logs.dto";

export const GET_LOGS_FEATURE_NAME = 'getLogs'

export interface LogsGetState {
  logsList: LogsDto[]
  error: string
}

export const LOGS_GET_INITIAL_STATE: LogsGetState = {
  logsList: [],
  error: ''
}
