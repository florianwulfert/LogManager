import {Injectable} from "@angular/core";
import {Store} from "@ngrx/store";
import {addUser, deleteUsers, getUsers} from "./user.selector";
import {addUserAction, deleteUserAction, getUsersAction} from "./user.actions";
import {AddUserRequest} from "./addUser/dto/add-user-request";
import {UserGetState} from "./getUser/store/user-get.state";
import {UserAddState} from "./addUser/store/user-add.state";
import {UserDeleteState} from "./deleteUser/store/user-delete.state";

@Injectable({providedIn: 'root'})
export class UserFacade {
  stateGetUserResponse$ = this.userGetState.select(getUsers);
  stateAddUser$ = this.userAddState.select(addUser);
  stateDeleteUser$ = this.userDeleteState.select(deleteUsers);

  constructor(
    private readonly userGetState: Store<UserGetState>,
    private readonly userAddState: Store<UserAddState>,
    private readonly userDeleteState: Store<UserDeleteState>
  ) {
  }

  getUser(): void {
    this.userGetState.dispatch(getUsersAction());
  }

  addUser(request: AddUserRequest): void {
    this.userAddState.dispatch(addUserAction(request));
  }

  deleteUser(): void {
    this.userDeleteState.dispatch(deleteUserAction());
  }
}
