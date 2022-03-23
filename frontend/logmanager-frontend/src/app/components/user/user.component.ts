import {Component, OnDestroy, OnInit} from '@angular/core';
import {UserFacade} from "../../modules/user/user.facade";
import {SubscriptionManager} from "../../../assets/utils/subscription.manager";
import {MatTableDataSource} from "@angular/material/table";
import {MatSnackBar} from "@angular/material/snack-bar";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AddUserRequest} from "../../modules/user/addUser/dto/add-user-request";
import {FeatureManager} from "../../../assets/utils/feature.manager";

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit, OnDestroy {

  constructor(private userFacade: UserFacade, private _snackBar: MatSnackBar) {
  }

  subscriptionManager = new SubscriptionManager();
  featureManager = new FeatureManager(this._snackBar);
  returnUserMessage: string | undefined;

  displayedColumns: string[] = ['id', 'name', 'birthdate', 'weight', 'height', 'favouriteColor', 'bmi', 'delete'];
  dataSource: any;
  emptyFieldMessage: string = 'Please fill all fields'

  ngOnInit(): void {
    this.userFacade.getUser();
    this.subscriptionManager.add(this.userFacade.stateGetUserResponse$).subscribe(result => {
      this.dataSource = new MatTableDataSource(result)
    });
  }

  ngOnDestroy(): void {
    this.subscriptionManager.clear();
  }

  position = new FormControl('above');

  public form: FormGroup = new FormGroup({
    name: new FormControl('', [Validators.required]),
    birthdate: new FormControl('', [Validators.required]),
    height: new FormControl('', [Validators.required]),
    weight: new FormControl('', [Validators.required]),
    favouriteColor: new FormControl('', [Validators.required])
  })

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  prepareAddUserRequest(request: AddUserRequest): AddUserRequest {
    request.name = this.form.get("name")?.value
    request.birthdate = this.form.get("birthdate")?.value
    request.weight = this.form.get("weight")?.value
    request.height = this.form.get("height")?.value
    request.favouriteColor = this.form.get("favouriteColor")?.value
    return request;
  }

  createUser(): void {
    let request = new AddUserRequest
    request = this.prepareAddUserRequest(request)
    this.userFacade.addUser(request);
    this.subscriptionManager.add(this.userFacade.stateAddUser$).subscribe(result => {
      this.returnUserMessage = result
    })
    this.featureManager.openSnackbar(this.returnUserMessage);
  }

  deleteUsers(): void {
    this.userFacade.deleteUser();
    this.subscriptionManager.add(this.userFacade.stateDeleteUser$).subscribe(result => {
      this.returnUserMessage = result
    })
    this.featureManager.openSnackbar(this.returnUserMessage);
  }
}
