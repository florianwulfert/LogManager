import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";
import {Injectable} from "@angular/core";

@Injectable({providedIn: 'root'})
export class FeatureManager {
  constructor(private _snackBar: MatSnackBar) {
  }

  openSnackbar(message: string | undefined, success: string): void {
    let horizontalPosition: MatSnackBarHorizontalPosition = 'center';
    let verticalPosition: MatSnackBarVerticalPosition = 'bottom';
    if (message) {
      this._snackBar.open(message, 'Close', {
        duration: 7000,
        horizontalPosition: horizontalPosition,
        verticalPosition: verticalPosition,
        panelClass: [success]
      });
    }
  }
}
