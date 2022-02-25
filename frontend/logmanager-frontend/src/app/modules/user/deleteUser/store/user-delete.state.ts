export const DELETE_USER_FEATURE_NAME = 'deleteUser';

export interface UserDeleteState {
  response: string;
  error: string;
}

export const USER_DELETE_INITIAL_STATE: UserDeleteState = {
  response: '',
  error: ''
};
