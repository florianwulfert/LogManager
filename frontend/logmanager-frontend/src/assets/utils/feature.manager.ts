import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";

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
