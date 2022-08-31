import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {CalculateBmiRequest} from "../../modules/bmi/calculate-bmi/dto/calculate-bmi-request";
import {BmiFacade} from "../../modules/bmi/bmi.facade";
import {MatSnackBar} from "@angular/material/snack-bar";
import {takeUntil} from "rxjs/operators";
import {Subject} from "rxjs";
import {UsersFacade} from "../../modules/users/users.facade";
import {UserFacade} from "../../modules/user/user.facade";
import {GetUserRequest} from "../../modules/user/getUser/getUser-request";
import {ActorFacade} from "../../modules/actor/actor.facade";

@Component({
  selector: 'app-bmi',
  templateUrl: './bmi.component.html',
  styleUrls: ['./bmi.component.scss']
})
export class BmiComponent implements OnDestroy, OnInit {

  constructor(private bmiFacade: BmiFacade,
              private _snackBar: MatSnackBar,
              private usersFacade: UsersFacade,
              private userFacade: UserFacade,
              private actorFacade: ActorFacade) {
  }

  ngOnDestroy() {
    this.onDestroy.next(null)
    this.onDestroy.complete()
  }

  ngOnInit() {
    this.getUserList()
    this.actorFacade.stateActorIsValid$.pipe(takeUntil(this.onDestroy)).subscribe(r => {
      if (r) {
        this.userAvailable = true
      }
    })
  }

  returnUserMessage: string | undefined
  onDestroy = new Subject()
  users: any
  usersBmi: void | undefined
  userAvailable: boolean = false


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

  getUserList(): void {
    this.usersFacade.getUsers();
    this.usersFacade.stateGetUsersResponse$.pipe(takeUntil(this.onDestroy)).subscribe(result => {
      this.users = result
    });
  }

  public formUser: FormGroup = new FormGroup({
    user: new FormControl('', [Validators.required]),
    bmi: new FormControl('')
  })

  getUsersData(name: string) {
    let getRequest = new GetUserRequest()
    getRequest.name = name
    this.userFacade.getUser(getRequest)
    this.userFacade.stateGetUserResponse$.pipe(takeUntil(this.onDestroy)).subscribe(result => {
      this.usersBmi = this.formUser.get("bmi")?.setValue(result.bmi)

    })
  }
}
