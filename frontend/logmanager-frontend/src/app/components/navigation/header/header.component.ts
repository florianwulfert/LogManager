import {Component, EventEmitter, OnInit, Output, ViewChild} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {MatMenuTrigger} from "@angular/material/menu";
import {ProfileMenuComponent} from "../profile-menu/profile-menu.component";
import {ActorState} from "../../../modules/actor/actor.state";
import {Store} from "@ngrx/store";
import {SubscriptionManager} from "../../../../assets/utils/subscription.manager";
import {ActorFacade} from "../../../modules/actor/actor.facade";
import {ActorDto} from "../../../modules/actor/actor.dto";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  name: string | undefined

  @Output() public sidenavToggle = new EventEmitter();
  @ViewChild('menuTrigger')
  menuTrigger!: MatMenuTrigger;

  constructor(public dialog: MatDialog, private readonly actorState: Store<ActorState>, private actorFacade: ActorFacade) {
  }

  subscriptionManager = new SubscriptionManager();
  actorDto: ActorDto = new ActorDto()

  ngOnInit(): void {
  }

  public onToggleSidenav = () => {
    this.sidenavToggle.emit();
  }

  openDialog() {
    const dialogRef = this.dialog.open(ProfileMenuComponent, {
      restoreFocus: false,
      data: {name: this.name},
    });
    dialogRef.afterClosed().subscribe((result) => {
      this.actorDto.actor = result
      this.actorFacade.saveActor(this.actorDto)
      this.subscriptionManager.add(this.actorFacade.stateActor$).subscribe(r => {
        this.name = r.actor
      })
      this.menuTrigger.focus()
    });
  }
}
