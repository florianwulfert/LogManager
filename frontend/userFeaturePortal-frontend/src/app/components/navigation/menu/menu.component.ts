import {Component, EventEmitter, OnDestroy, OnInit, Output} from '@angular/core';
import {ActorFacade} from "../../../modules/actor/actor.facade";
import {Subject} from "rxjs";
import {takeUntil} from "rxjs/operators";

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit, OnDestroy {

  @Output() sidenavClose = new EventEmitter();

  constructor(private actorFacade: ActorFacade) {
  }
  userAvailable: boolean = false
  onDestroy = new Subject()

  ngOnInit() {
    this.actorFacade.stateActorIsValid$.pipe(takeUntil(this.onDestroy)).subscribe(r => {
      if (r) {
        this.userAvailable = true
      }
    })
  }

  ngOnDestroy() {
    this.onDestroy.next(null)
    this.onDestroy.complete()
  }

  public onSidenavClose = () => {
    this.sidenavClose.emit();
  }

}
