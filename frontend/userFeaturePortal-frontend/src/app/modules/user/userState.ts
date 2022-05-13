import {UserDto} from "./getUsers/user.dto";

export const USER_FEATURE_NAME = 'user';

export interface UserState {
  response: UserDto
}

const user: UserDto = {
  id: 0,
  name: "",
  birthdate: "",
  height: 0,
  weight: 0,
  bmi: 0,
  favouriteBookTitel: ""
}

export const USER_GET_INITIAL_STATE: UserState = {
  response: user
}
