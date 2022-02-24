import {Component, OnInit} from '@angular/core';
import {SubscriptionManager} from "../../../../assets/utils/subscription.manager";
import {UserFacade} from "../../../modules/user/store/user.facade";
import {FormControl, FormGroup} from "@angular/forms";
import {AddUserRequest} from "../../../modules/user/dto/add-user-request";

@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.scss']
})
export class CreateUserComponent implements OnInit {

  constructor(private userFacade: UserFacade) { }

  subscriptionManager = new SubscriptionManager();
  returnUserMessage: string | undefined;

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
    let actor = "Peter"
    let name = this.form.get("name")?.value
    let birthdate = this.form.get("birthdate")?.value
    let weight = this.form.get("weight")?.value
    let height = this.form.get("height")?.value
    let favouriteColor = this.form.get("favouriteColor")?.value
    request.actor = actor
    request.name = name
    request.birthdate = birthdate
    request.weight = weight
    request.height = height
    request.favouriteColor = favouriteColor
    this.userFacade.addUser(request);

    this.subscriptionManager.add(this.userFacade.stateAddUser$).subscribe(result => {
      this.returnUserMessage = result;
    })
    window.location.reload()
  }

}
