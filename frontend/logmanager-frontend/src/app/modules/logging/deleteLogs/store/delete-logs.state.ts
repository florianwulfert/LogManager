export const DELETE_LOGS_FEATURE_NAME = 'deleteUser';

export interface LogsDeleteState {
  response: string;
  error: string;
}

export const LOGS_DELETE_INITIAL_STATE: LogsDeleteState = {
  response: '',
  error: ''
};
