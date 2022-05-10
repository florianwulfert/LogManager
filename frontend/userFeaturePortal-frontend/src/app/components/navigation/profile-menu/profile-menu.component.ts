import {Component, Inject, Injectable, OnDestroy} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {HeaderComponent} from "../header/header.component";
import {Profile} from "./profile.interface";
import {ActorDto} from "../../../modules/actor/actor.dto";
import {ActorFacade} from "../../../modules/actor/actor.facade";
import {takeUntil} from "rxjs/operators";
import {Subject} from "rxjs";


@Component({
  selector: 'app-profile-menu',
  templateUrl: './profile-menu.component.html',
  styleUrls: ['./profile-menu.component.scss']
})

@Injectable({
  providedIn: 'root'
})
export class ProfileMenuComponent implements OnDestroy {

  errorMessage: string = ''
  actorDto: ActorDto = new ActorDto()

  isLoggedIn: boolean = false
  onDestroy = new Subject()

  constructor(
    public dialogRef: MatDialogRef<HeaderComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Profile,
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

  cancel(): void {
    this.dialogRef.close("not registered user");
  }

  validateLogin(): void {
    this.actorDto.actor = this.data.name
    this.actorFacade.getUserByName(this.actorDto)
    this.actorFacade.stateActorIsValid$.pipe(takeUntil(this.onDestroy)).subscribe(r => {
      if (r) {
        this.dialogRef.close(this.data.name)
      }
      this.errorMessage = 'User not registered yet!\nPlease contact an administrator.'
    })
  }

}
