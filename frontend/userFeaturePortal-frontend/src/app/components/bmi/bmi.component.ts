import {Component, OnDestroy} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {CalculateBmiRequest} from "../../modules/bmi/calculate-bmi/dto/calculate-bmi-request";
import {BmiFacade} from "../../modules/bmi/bmi.facade";
import {MatSnackBar} from "@angular/material/snack-bar";
import {SubscriptionManager} from "../../../assets/utils/subscription.manager";
import {takeUntil} from "rxjs/operators";
import {Subject} from "rxjs";

@Component({
  selector: 'app-bmi',
  templateUrl: './bmi.component.html',
  styleUrls: ['./bmi.component.scss']
})
export class BmiComponent implements OnDestroy{

  constructor(private bmiFacade: BmiFacade, private _snackBar: MatSnackBar) {
  }

  ngOnDestroy() {
    this.onDestroy.next(null)
    this.onDestroy.complete()
  }

  subscriptionManager = new SubscriptionManager()
  returnUserMessage: string | undefined
  onDestroy = new Subject()


  public form: FormGroup = new FormGroup({
    weight: new FormControl('', [Validators.required]),
    height: new FormControl('', [Validators.required]),
    birthdate: new FormControl('', [Validators.required])
  })

  prepareCalcBmiRequest(request: CalculateBmiRequest): CalculateBmiRequest {
    request.weight = this.form.get("weight")?.value
    request.height = this.form.get("height")?.value
    request.birthdate = this.form.get("birthdate")?.value
    return request;
  }

  calculateBmi(): void {
    let request = new CalculateBmiRequest()
    request = this.prepareCalcBmiRequest(request)
    this.bmiFacade.calcBmi(request);
    this.bmiFacade.stateCalcBmi$.pipe(takeUntil(this.onDestroy)).subscribe(result => {
      this.returnUserMessage = result
    })
  }

}
