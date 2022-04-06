import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {HeaderComponent} from "../header/header.component";
import {Profile} from "./profile.interface";
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-profile-menu',
  templateUrl: './profile-menu.component.html',
  styleUrls: ['./profile-menu.component.scss']
})
export class ProfileMenuComponent {

  errorMessage: string = ''

  constructor(
    public dialogRef: MatDialogRef<HeaderComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Profile,
  ) { }

  cancel(): void {
    this.dialogRef.close();
  }

  validateLogin(): void {
    console.log(this.data)
    if (this.data.name === 'Flo') {
      this.dialogRef.close(this.data.name)
    }
    this.errorMessage = 'User not registered yet!\nPlease contact an administrator.'
  }

  public form: FormGroup = new FormGroup({
    userName: new FormControl('', [Validators.required]),
  })
}
