import {Component, OnInit} from '@angular/core';
import {SubscriptionManager} from "../../../../assets/utils/subscription.manager";
import {UserFacade} from "../../../modules/user/store/user.facade";
import {FormControl, FormGroup} from "@angular/forms";
import {AddUserRequest} from "../../../modules/user/dto/add-user-request";
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";

@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.scss']
})
export class CreateUserComponent implements OnInit {

  constructor(private userFacade: UserFacade, private _snackBar: MatSnackBar) { }

  subscriptionManager = new SubscriptionManager();
  returnUserMessage: string | undefined;

  horizontalPosition: MatSnackBarHorizontalPosition = 'center';
  verticalPosition: MatSnackBarVerticalPosition = 'top';

  ngOnInit(): void {
  }

  public form: FormGroup = new FormGroup({
    name: new FormControl(),
    birthdate: new FormControl(),
    height: new FormControl(),
    weight: new FormControl(),
    favouriteColor: new FormControl()
  })

  createUser(): void {
    let request = new AddUserRequest
    request.actor = "Peter"
    request.name = this.form.get("name")?.value
    request.birthdate = this.form.get("birthdate")?.value
    request.weight = this.form.get("weight")?.value
    request.height = this.form.get("height")?.value
    request.favouriteColor = this.form.get("favouriteColor")?.value
    this.userFacade.addUser(request);
    this.subscriptionManager.add(this.userFacade.stateAddUser$).subscribe(result => {
      this.returnUserMessage = result;
      console.log(this.returnUserMessage)
    })
    if (this.returnUserMessage != null) {
      this._snackBar.open(this.returnUserMessage, 'Close', {
        horizontalPosition: this.horizontalPosition,
        verticalPosition: this.verticalPosition,
      });
    }
  }
}
