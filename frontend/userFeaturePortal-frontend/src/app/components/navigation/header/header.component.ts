import {Component, EventEmitter, OnDestroy, OnInit, Output, ViewChild} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {MatMenuTrigger} from "@angular/material/menu";
import {ProfileMenuComponent} from "../profile-menu/profile-menu.component";
import {ActorState} from "../../../modules/actor/actor.state";
import {Store} from "@ngrx/store";
import {ActorFacade} from "../../../modules/actor/actor.facade";
import {ActorDto} from "../../../modules/actor/actor.dto";
import {Subject} from "rxjs";
import {takeUntil} from "rxjs/operators";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, OnDestroy {

  name: string | undefined

  @Output() public sidenavToggle = new EventEmitter();
  @ViewChild('menuTrigger')
  menuTrigger!: MatMenuTrigger;

  constructor(public dialog: MatDialog, private readonly actorState: Store<ActorState>, private actorFacade: ActorFacade) {
  }

  actorDto: ActorDto = new ActorDto()
  onDestroy = new Subject()

  ngOnInit(): void {
    this.openDialog()
  }

  ngOnDestroy() {
    this.onDestroy.next(null)
    this.onDestroy.complete()
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
      this.actorFacade.stateActorIsValid$.pipe(takeUntil(this.onDestroy)).subscribe(r => {
        if (!r && result !== "not registered user") {
          window.location.assign('http://localhost:4200/home');
        }
      })
      this.actorDto.actor = result
      this.actorFacade.saveActor(this.actorDto)
      this.actorFacade.stateActor$.pipe(takeUntil(this.onDestroy)).subscribe(r => {
        this.name = r
      })
      this.menuTrigger.focus()
    });
  }
}
