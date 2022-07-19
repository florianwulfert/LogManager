import {Component, EventEmitter, OnDestroy, Output, ViewChild} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {MatMenuTrigger} from "@angular/material/menu";
import {ActorState} from "../../../modules/actor/actor.state";
import {Store} from "@ngrx/store";
import {ActorFacade} from "../../../modules/actor/actor.facade";
import {Subject} from "rxjs";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnDestroy {

  name: string | undefined

  @Output() public sidenavToggle = new EventEmitter();
  @ViewChild('menuTrigger')
  menuTrigger!: MatMenuTrigger;

  constructor(public dialog: MatDialog, private readonly actorState: Store<ActorState>, private actorFacade: ActorFacade) {
  }

  onDestroy = new Subject()

  ngOnDestroy() {
    this.onDestroy.next(null)
    this.onDestroy.complete()
  }

  public onToggleSidenav = () => {
    this.sidenavToggle.emit();
  }
}
