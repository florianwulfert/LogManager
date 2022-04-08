import {Component, OnInit} from '@angular/core';
import {SubscriptionManager} from "../../../assets/utils/subscription.manager";
import {ActorFacade} from "../../modules/actor/actor.facade";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  subscriptionManager = new SubscriptionManager();
  userAvailable: boolean = false

  constructor(private readonly actorFacade: ActorFacade) {
  }

  ngOnInit() {
    this.subscriptionManager.add(this.actorFacade.stateActorIsValid$).subscribe(r => {
      if (r === true && r !== undefined) {
        this.userAvailable = true
      }
    })
  }

  bmiDescription = 'Click here to calculate your BMI according to your weight and height.'
  userDescription = 'Click here to search a user, create one or delete one.'
  loggingDescription = 'Click here to see which actions took place in the User Manager.'
  bookDescription = 'Click here to find, delete or add books.'
}
