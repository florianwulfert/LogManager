export const DELETE_LOG_FEATURE_NAME = 'deleteUser';

export interface LogDeleteState {
  response: string;
  error: string;
}

export const LOG_DELETE_INITIAL_STATE: LogDeleteState = {
  response: '',
  error: ''
};
