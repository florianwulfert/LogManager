import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";
import {Injectable} from "@angular/core";
import {FormGroup} from "@angular/forms";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {Router} from "@angular/router";

@Injectable({providedIn: 'root'})
export class FeatureManager {
  constructor(private _snackBar: MatSnackBar, private readonly router: Router, private readonly http: HttpClient) {
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

  addItemToList(form: FormGroup, itemToAdd: string, list: string[]) {
    if (!form.get(itemToAdd)?.value) {
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

  login() {
    let headers = new HttpHeaders()
    headers.set('access-control-allow-origin', "http://localhost:8081")
    this.http.post<Observable<boolean>>("http://localhost:8081/login", {
      name: "devs",
      password: "Test"
    }).subscribe(isValid => {
      if (isValid) {
        sessionStorage.setItem(
          'token',
          btoa("devs" + ':' + "Test")
        );
      } else {
        alert("Authentication failed.")
      }
    });
  }
}
