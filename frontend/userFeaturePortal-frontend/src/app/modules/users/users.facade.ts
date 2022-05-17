import {Injectable} from "@angular/core";
import {Store} from "@ngrx/store";
import {getUsers} from "./users.selector";
import {addUserAction, deleteUserAction, deleteUsersAction, getUsersAction} from "./users.actions";
import {AddUserRequest} from "./addUser/add-user-request";
import {UsersState} from "./usersState";
import {DeleteUserRequest} from "./deleteUser/delete-user-request";

@Injectable({providedIn: 'root'})
export class UsersFacade {
  stateGetUsersResponse$ = this.usersState.select(getUsers)

  constructor(
    private readonly usersState: Store<UsersState>,
  ) {}

  getUsers(): void {
    this.usersState.dispatch(getUsersAction());
  }

  addUser(request: AddUserRequest): void {
    this.usersState.dispatch(addUserAction(request));
  }

  deleteUsers(): void {
    this.usersState.dispatch(deleteUsersAction());
  }

  deleteUser(request: DeleteUserRequest): void {
    this.usersState.dispatch(deleteUserAction(request));
  }
}
