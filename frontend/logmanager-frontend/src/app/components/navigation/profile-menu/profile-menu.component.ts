import {Component, Inject, Injectable, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {HeaderComponent} from "../header/header.component";
import {Profile} from "./profile.interface";
import {SubscriptionManager} from "../../../../assets/utils/subscription.manager";
import {ActorDto} from "../../../modules/actor/actor.dto";
import {ActorFacade} from "../../../modules/actor/actor.facade";


@Component({
  selector: 'app-profile-menu',
  templateUrl: './profile-menu.component.html',
  styleUrls: ['./profile-menu.component.scss']
})

@Injectable({
  providedIn: 'root'
})
export class ProfileMenuComponent implements OnInit {

  errorMessage: string = ''
  actorDto: ActorDto = new ActorDto()

  subscriptionManager = new SubscriptionManager();
  isLoggedIn: boolean = false;

  constructor(
    public dialogRef: MatDialogRef<HeaderComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Profile,
    private readonly actorFacade: ActorFacade,
  ) {
  }

  ngOnInit() {
    this.subscriptionManager.add(this.actorFacade.stateActor$).subscribe(r => {
      if(r !== "" && r !== "not registered user") {
        this.isLoggedIn = true
      }
    })
  }

  cancel(): void {
    this.dialogRef.close("not registered user");
  }

  validateLogin(): void {
    this.actorDto.actor = this.data.name
    this.actorFacade.getUserByName(this.actorDto)
    this.subscriptionManager.add(this.actorFacade.stateActorIsValid$).subscribe(r => {
      if (r === true) {
        this.dialogRef.close(this.data.name)
      }
      this.errorMessage = 'User not registered yet!\nPlease contact an administrator.'
    })
  }

}
