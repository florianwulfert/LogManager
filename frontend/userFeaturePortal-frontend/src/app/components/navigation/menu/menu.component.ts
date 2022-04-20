import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {ActorFacade} from "../../../modules/actor/actor.facade";
import {SubscriptionManager} from "../../../../assets/utils/subscription.manager";

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit{

  @Output() sidenavClose = new EventEmitter();

  constructor(private actorFacade: ActorFacade) {
  }
  subscriptionManager = new SubscriptionManager();
  userAvailable: boolean = false

  ngOnInit() {
    this.subscriptionManager.add(this.actorFacade.stateActorIsValid$).subscribe(r => {
      if (r === true && r !== undefined) {
        this.userAvailable = true
      }
    })
  }

  public onSidenavClose = () => {
    this.sidenavClose.emit();
  }

}
