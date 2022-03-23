import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";
import {Injectable} from "@angular/core";

@Injectable({providedIn: 'root'})
export class FeatureManager {
  constructor(private _snackBar: MatSnackBar) {
  }

  openSnackbar(message: string | undefined): void {
    let horizontalPosition: MatSnackBarHorizontalPosition = 'center';
    let verticalPosition: MatSnackBarVerticalPosition = 'bottom';
    if (message != null && message != '') {
      this._snackBar.open(message, 'Close', {
        horizontalPosition: horizontalPosition,
        verticalPosition: verticalPosition
      });
    }
  }
}
