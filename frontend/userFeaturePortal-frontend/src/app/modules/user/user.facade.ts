import {Injectable} from "@angular/core";
import {Store} from "@ngrx/store";
import {getUser} from "./user.selector";
import {UserState} from "./userState";
import {getUserAction} from "./user.actions";
import {GetUserRequest} from "./getUser/getUser-request";

@Injectable({providedIn: 'root'})
export class UserFacade {
  stateGetUserResponse$ = this.userState.select(getUser)

  constructor(
    private readonly userState: Store<UserState>,
  ) {}

  getUser(request: GetUserRequest): void {
    this.userState.dispatch(getUserAction(request))
  }
}
