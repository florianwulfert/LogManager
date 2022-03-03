import {Component, EventEmitter, OnInit, Output, ViewChild} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {MatMenuTrigger} from "@angular/material/menu";
import {ProfileMenuComponent} from "../profile-menu/profile-menu.component";

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

  constructor(public dialog: MatDialog) {
  }

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
      this.menuTrigger.focus()
    });
  }
}
