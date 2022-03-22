export const DELETE_USERS_FEATURE_NAME = 'deleteUsers';

export interface UsersDeleteState {
  response: string;
  error: string;
}

export const USERS_DELETE_INITIAL_STATE: UsersDeleteState = {
  response: '',
  error: ''
};
