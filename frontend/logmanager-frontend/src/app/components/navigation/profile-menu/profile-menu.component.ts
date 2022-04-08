import {Component, Inject, Injectable} from '@angular/core';
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
export class ProfileMenuComponent {

  errorMessage: string = ''
  actorDto: ActorDto = new ActorDto()

  subscriptionManager = new SubscriptionManager();

  constructor(
    public dialogRef: MatDialogRef<HeaderComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Profile,
    private readonly actorFacade: ActorFacade,
  ) {
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
