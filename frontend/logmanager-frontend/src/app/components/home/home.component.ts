import {Component} from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {

  constructor() {
  }

  bmiDescription = 'Click here to calculate your BMI according to your weight and height or see the BMI of another user.'
  userDescription = 'Click here to search a user, create one or delete one.'
  loggingDescription = 'Click here to see which actions took place in the User Manager.'
}
