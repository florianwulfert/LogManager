import {Injectable} from "@angular/core";
import {Store} from "@ngrx/store";
import {getUsers} from "./user.selector";
import {addUserAction, deleteUserAction, deleteUsersAction, getUsersAction} from "./user.actions";
import {AddUserRequest} from "./addUser/add-user-request";
import {UserState} from "./user.state";
import {DeleteUserRequest} from "./deleteUser/delete-user-request";

@Injectable({providedIn: 'root'})
export class UserFacade {
  stateGetUserResponse$ = this.userState.select(getUsers)

  constructor(
    private readonly userState: Store<UserState>,
  ) {}

  getUsers(): void {
    this.userState.dispatch(getUsersAction());
  }

  addUser(request: AddUserRequest): void {
    this.userState.dispatch(addUserAction(request));
  }

  deleteUsers(): void {
    this.userState.dispatch(deleteUsersAction());
  }

  deleteUser(request: DeleteUserRequest): void {
    this.userState.dispatch(deleteUserAction(request));
  }
}
