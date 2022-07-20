import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActorFacade} from "../../../modules/actor/actor.facade";
import {takeUntil} from "rxjs/operators";
import {Observable, Subject} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Router} from "@angular/router";

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
    private readonly router: Router
  ) {
    this.actorFacade.stateActor$.pipe(takeUntil(this.onDestroy)).subscribe(r => {
      if (r !== "" && r !== "not registered user") {
        this.isLoggedIn = true
      }
    })
  }

  ngOnDestroy() {
    this.onDestroy.next(null)
    this.onDestroy.complete()
  }

  ngOnInit() {
    this.login()
  }

  login() {
    let headers = new HttpHeaders()
    headers.set('access-control-allow-origin', "http://localhost:8081")
    this.http.post<Observable<boolean>>("http://localhost:8081/login", {
      name: "devs",
      password: "Test"
    }).subscribe(isValid => {
      if (isValid) {
        sessionStorage.setItem(
          'token',
          btoa("devs" + ':' + "Test")
        );
        this.router.navigate(['/home']);
      } else {
        alert("Authentication failed.")
      }
    });
  }
}
