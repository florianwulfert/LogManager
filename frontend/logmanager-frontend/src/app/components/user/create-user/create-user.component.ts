import {Component, OnInit} from '@angular/core';
import {SubscriptionManager} from "../../../../assets/utils/subscription.manager";
import {UserFacade} from "../../../modules/user/store/user.facade";
import {FormControl, FormGroup} from "@angular/forms";

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
    let name = this.form.get("name")?.value
    this.userFacade.addUser(name);
    this.subscriptionManager.add(this.userFacade.stateAddUser$).subscribe(result => {
      this.returnUserMessage = result;
    })
  }

}
