import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActorFacade} from "../../modules/actor/actor.facade";
import {Subject} from "rxjs";
import {takeUntil} from "rxjs/operators";
import {UserFacade} from "../../modules/user/user.facade";
import {FeatureManager} from "../../../assets/utils/feature.manager";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, OnDestroy {

  onDestroy = new Subject()

  constructor(private readonly features: FeatureManager) {
  }

  ngOnInit() {
    this.features.login()
  }

  ngOnDestroy() {
    this.onDestroy.next(null)
    this.onDestroy.complete()
  }

  bmiDescription = 'Click here to calculate your BMI according to your weight and height.'
  userDescription = 'Click here to search a user, create one or delete one.'
  bibelleseDescription = 'Click here to see current entries about reading bibel, create entries or delete one.'
  loggingDescription = 'Click here to see which actions took place in the User Feature Portal.'
  bookDescription = 'Click here to find, delete or add books to a specific user.'
}
