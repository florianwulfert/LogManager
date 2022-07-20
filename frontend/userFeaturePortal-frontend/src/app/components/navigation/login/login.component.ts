import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActorFacade} from "../../../modules/actor/actor.facade";
import {takeUntil} from "rxjs/operators";
import {Subject} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Login} from "./login.interface";


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})

export class LoginComponent implements OnDestroy, OnInit {

  errorMessage: string = ''

  isLoggedIn: boolean = false
  onDestroy = new Subject()

  constructor(
    private readonly http: HttpClient,
    private readonly actorFacade: ActorFacade,
  ) {
    this.actorFacade.stateActor$.pipe(takeUntil(this.onDestroy)).subscribe(r => {
      if(r !== "" && r !== "not registered user") {
        this.isLoggedIn = true
      }
    })
  }

  ngOnDestroy() {
    this.onDestroy.next(null)
    this.onDestroy.complete()
  }

  ngOnInit() {
    let headers = new HttpHeaders()
    headers.set('access-control-allow-origin', "http://localhost:8081")
    this.http.post("http://localhost:8081/login", {
      "user": {
        "name":"devs",
        "password":"Test"
      }
    }).subscribe()
  }

  login() {
    console.log("Login")

  }
}
