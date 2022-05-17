import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {UsersFacade} from "../../modules/users/users.facade";
import {MatTableDataSource} from "@angular/material/table";
import {MatSnackBar} from "@angular/material/snack-bar";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AddUserRequest} from "../../modules/users/addUser/add-user-request";
import {DeleteUserRequest} from "../../modules/users/deleteUser/delete-user-request";
import {MatPaginator} from "@angular/material/paginator";
import {BooksFacade} from "../../modules/books/books.facade";
import {Subject} from "rxjs";
import {takeUntil} from "rxjs/operators";
import {GetUserRequest} from "../../modules/user/getUser/getUser-request";
import {UserFacade} from "../../modules/user/user.facade";

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit, OnDestroy {

  constructor(private usersFacade: UsersFacade,
              private _snackBar: MatSnackBar,
              private booksFacade: BooksFacade,
              private userFacade: UserFacade) {
  }

  displayedColumns: string[] = ['name', 'birthdate', 'weight', 'height', 'bmi', 'favouriteBook', 'delete']
  dataSource: any
  books: any
  onDestroy = new Subject()
  users: any

  name: string | undefined
  birthdate: string = "2002.03.08"
  favouriteBook: string | undefined
  height: number | undefined
  weight: number | undefined

  @ViewChild(MatPaginator) paginator: MatPaginator | undefined;

  ngOnInit(): void {
    this.getUserList()
    this.getBooks()
  }

  ngOnDestroy(): void {
    this.onDestroy.next(null)
    this.onDestroy.complete()
  }

  position = new FormControl('above');

  public form: FormGroup = new FormGroup({
    name: new FormControl('', [Validators.required]),
    birthdate: new FormControl('', [Validators.required]),
    height: new FormControl('', [Validators.required]),
    weight: new FormControl('', [Validators.required]),
    favouriteBook: new FormControl('')
  })


  public prefillForm: FormGroup = new FormGroup({
    name: new FormControl('', [Validators.required]),
    birthdate: new FormControl('', [Validators.required]),
    height: new FormControl('', [Validators.required]),
    weight: new FormControl('', [Validators.required]),
    favouriteBook: new FormControl('')
  })

  preFillForm(): void {
    this.prefillForm.patchValue({
      name: "frfe",
      height: 1.8,

    })
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  prepareAddUserRequest(request: AddUserRequest): AddUserRequest {
    request.name = this.form.get("name")?.value
    request.birthdate = this.form.get("birthdate")?.value
    request.weight = this.form.get("weight")?.value
    request.height = this.form.get("height")?.value
    request.favouriteBook = this.form.get("favouriteBook")?.value
    return request
  }

  createUser(): void {
    let request = new AddUserRequest
    request = this.prepareAddUserRequest(request)
    this.usersFacade.addUser(request)
  }

  deleteUsers(): void {
    this.usersFacade.deleteUsers()
  }

  deleteUser(element: string): void {
    let request = new DeleteUserRequest
    request.name = element
    this.usersFacade.deleteUser(request)
  }

  getUserList(): void {
    this.usersFacade.getUsers();
    this.usersFacade.stateGetUsersResponse$.pipe(takeUntil(this.onDestroy)).subscribe(result => {
      this.dataSource = new MatTableDataSource(result)
      this.dataSource.paginator = this.paginator;
      this.users = result
    });
  }

  getBooks(): void {
    this.booksFacade.getBooks();
    this.booksFacade.stateGetBooksResponse$.pipe(takeUntil(this.onDestroy)).subscribe(result => {
     this.books = result
    });
  }

  updateUser(): void {
    let request = new AddUserRequest
    request = this.prepareAddUserRequest(request)
    this.usersFacade.updateUser(request)

  }


  getUsersData(name: string): void {
    let getRequest = new GetUserRequest()
    getRequest.name = name
    this.userFacade.getUser(getRequest)
    this.userFacade.stateGetUserResponse$.pipe(takeUntil(this.onDestroy)).subscribe(result => {
    })
  }
}
