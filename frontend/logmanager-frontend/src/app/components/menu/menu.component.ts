import {Component, EventEmitter, Output} from '@angular/core';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent {

  @Output() sidenavClose = new EventEmitter();

  constructor() {
  }

  public onSidenavClose = () => {
    this.sidenavClose.emit();
  }

}
