import {Injectable} from "@angular/core";
import {Store} from "@ngrx/store";
import {addUser, deleteUser, deleteUsers, getUsers} from "./user.selector";
import {addUserAction, deleteUserAction, deleteUsersAction, getUsersAction} from "./user.actions";
import {AddUserRequest} from "./addUser/dto/add-user-request";
import {UserGetState} from "./getUser/store/user-get.state";
import {UserAddState} from "./addUser/store/user-add.state";
import {UserDeleteState} from "./deleteUser/store/user-delete.state";
import {DeleteUserRequest} from "./deleteUser/dto/delete-user-request";

@Injectable({providedIn: 'root'})
export class UserFacade {
  stateGetUserResponse$ = this.userGetState.select(getUsers)
  stateAddUser$ = this.userAddState.select(addUser)
  stateDeleteUsers$ = this.usersDeleteState.select(deleteUsers)
  stateDeleteUser$ = this.userDeleteState.select(deleteUser)

  constructor(
    private readonly userGetState: Store<UserGetState>,
    private readonly userAddState: Store<UserAddState>,
    private readonly usersDeleteState: Store<UserDeleteState>,
    private readonly userDeleteState: Store<UserDeleteState>
  ) {
  }

  getUser(): void {
    this.userGetState.dispatch(getUsersAction());
  }

  addUser(request: AddUserRequest): void {
    this.userAddState.dispatch(addUserAction(request));
  }

  deleteUsers(): void {
    this.usersDeleteState.dispatch(deleteUsersAction());
  }

  deleteUser(request: DeleteUserRequest): void {
    this.userDeleteState.dispatch(deleteUserAction(request));
  }
}
