import {Injectable} from "@angular/core";
import {Store} from "@ngrx/store";
import {addUser, getUsers} from "./user.selector";
import {addUserAction, deleteUserAction, deleteUsersAction, getUsersAction} from "./user.actions";
import {AddUserRequest} from "./addUser/dto/add-user-request";
import {UserGetState} from "./getUser/store/user-get.state";
import {UserAddState} from "./addUser/store/user-add.state";
import {DeleteUserRequest} from "./deleteUser/dto/delete-user-request";

@Injectable({providedIn: 'root'})
export class UserFacade {
  stateGetUserResponse$ = this.userGetState.select(getUsers)
  stateAddUser$ = this.userAddState.select(addUser)

  constructor(
    private readonly userGetState: Store<UserGetState>,
    private readonly userAddState: Store<UserAddState>,
  ) {
  }

  getUser(): void {
    this.userGetState.dispatch(getUsersAction());
  }

  addUser(request: AddUserRequest): void {
    this.userAddState.dispatch(addUserAction(request));
  }

  deleteUsers(): void {
    this.userGetState.dispatch(deleteUsersAction());
  }

  deleteUser(request: DeleteUserRequest): void {
    this.userGetState.dispatch(deleteUserAction(request));
  }
}
