import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";
import {Injectable} from "@angular/core";
import {FormGroup} from "@angular/forms";

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

  addItemToList(form: FormGroup, itemToAdd: string, list: string[], isForm: boolean) {
    if (isForm && !form.get(itemToAdd)?.value) {
      this.openSnackbar("Value for " + itemToAdd + " must be filled", "failed")
      return;
    }
    list.push(form.get(itemToAdd)?.value);
    form.get(itemToAdd)?.reset();
  }

  deleteItemFromList(itemToDelete: string, list: string[]) {
    let count = 0;
    for (let item of list) {
      if (item === itemToDelete) {
        list.splice(count, 1)
        return
      }
      count++
    }
  }
}
