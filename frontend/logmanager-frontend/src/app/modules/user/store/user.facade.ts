import {Injectable} from "@angular/core";
import {Store} from "@ngrx/store";
import {UserInteraction, UserState} from "./user-state";
import {addUser, getUsers} from "./user.selector";
import {addUserAction, getUsersAction} from "./user.actions";
import {AddUserRequest} from "../dto/add-user-request";

@Injectable({ providedIn: 'root' })
export class UserFacade {
  stateGetUserResponse$ = this.userState.select(getUsers);
  stateAddUser$ = this.userInteraction.select(addUser);

  constructor(private readonly userState: Store<UserState>, private readonly userInteraction: Store<UserInteraction>) {}

  getUser(): void {
    this.userState.dispatch(getUsersAction());
  }

  addUser(request: AddUserRequest): void {
    this.userInteraction.dispatch(addUserAction(request));
  }
}
