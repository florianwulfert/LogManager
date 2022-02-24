import {Component, OnDestroy, OnInit} from '@angular/core';
import {UserFacade} from "../../modules/user/store/user.facade";
import {SubscriptionManager} from "../../../assets/utils/subscription.manager";
import {MatTableDataSource} from "@angular/material/table";
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";
import {FormControl, FormGroup} from "@angular/forms";
import {AddUserRequest} from "../../modules/user/dto/add-user-request";

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit, OnDestroy {

  constructor(private userFacade: UserFacade, private _snackBar: MatSnackBar) {
  }

  subscriptionManager = new SubscriptionManager();
  returnUserMessage: string | undefined;

  horizontalPosition: MatSnackBarHorizontalPosition = 'center';
  verticalPosition: MatSnackBarVerticalPosition = 'top';

  displayedColumns: string[] = ['id', 'name', 'birthdate', 'weight', 'height', 'favouriteColor', 'bmi', 'delete'];
  listIsEmptyMessage: string = 'There are no users to show!';
  dataSource: any;

  ngOnInit(): void {
    this.userFacade.getUser();
    this.subscriptionManager.add(this.userFacade.stateGetUserResponse$).subscribe(result => {
      this.dataSource = new MatTableDataSource(result)
    });
  }

  ngOnDestroy(): void {
    this.subscriptionManager.clear();
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
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
      console.log(result)
      this.returnUserMessage = result
    })
    if (this.returnUserMessage != null) {
      this._snackBar.open(this.returnUserMessage, 'Close', {
        horizontalPosition: this.horizontalPosition,
        verticalPosition: this.verticalPosition,
      });
    }
  }
  position = new FormControl('above');
}
