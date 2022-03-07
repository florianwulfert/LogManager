import {UserAddState} from "../user/addUser/store/user-add.state";

export const ACTOR_FEATURE_NAME = 'actor';

export interface ActorState {
  response: string;
}

export const ACTOR_INITIAL_STATE: UserAddState = {
  response: '',
}
