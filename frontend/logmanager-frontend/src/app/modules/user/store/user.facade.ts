import {Injectable} from "@angular/core";
import {Store} from "@ngrx/store";
import {UserState} from "./user-state";
import {getUsers} from "./user.selector";
import {getUsersAction} from "./user.actions";

@Injectable({ providedIn: 'root' })
export class UserFacade {
  stateGetUserResponse$ = this.userState.select(getUsers);

  constructor(private readonly userState: Store<UserState>) {}

  getUser(): void {
    this.userState.dispatch(getUsersAction());
  }
}
