import {Component, OnDestroy, OnInit, ViewChild} from "@angular/core";
import {MatTableDataSource} from "@angular/material/table";
import {SubscriptionManager} from "../../../assets/utils/subscription.manager";
import {MatPaginator} from "@angular/material/paginator";
import {BooksFacade} from "../../modules/books/books.facade";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AddBookRequest} from "../../modules/books/addBooks/add-book-request";
import {MatSnackBar} from "@angular/material/snack-bar";
import {DeleteBookRequest} from "../../modules/books/deleteBook/delete-book-request";
import {UserFacade} from "../../modules/user/user.facade";
import {ActorFacade} from "../../modules/actor/actor.facade";

@Component({
  selector: 'app-book',
  templateUrl: './book.component.html',
  styleUrls: ['./book.component.scss']
})

export class BookComponent implements OnInit, OnDestroy {

  constructor(private booksFacade: BooksFacade, private _snackBar: MatSnackBar, private usersFacade: UserFacade, private actorFacade: ActorFacade) {
  }

  subscriptionManager = new SubscriptionManager();
  displayedColumns: string[] = ['id', 'titel', 'erscheinungsjahr', 'delete'];
  books: any
  users: any
  userAvailable: boolean = false
  booksListAvailable: boolean = false

  dataSource: any;

  @ViewChild(MatPaginator) paginator: MatPaginator | undefined;

  ngOnInit() {
    this.getBooks()
    this.subscriptionManager.add(this.actorFacade.stateActorIsValid$).subscribe(r => {
      if (r === true && r !== undefined) {
        this.userAvailable = true
      }
    })
    this.subscriptionManager.add(this.booksFacade.stateGetBooksResponse$).subscribe(result => {
      if (result.length > 0) {
        this.booksListAvailable = true
      }
    })
  }

  ngOnDestroy() {
    this.subscriptionManager.clear()
  }

  position = new FormControl('above');

  getBooks(): void {
    this.booksFacade.getBooks()
    this.subscriptionManager.add(this.booksFacade.stateGetBooksResponse$).subscribe(result => {
      this.dataSource = new MatTableDataSource(result)
      this.dataSource.paginator = this.paginator;
      this.books = result
    })
  }

  public form: FormGroup = new FormGroup({
    titel: new FormControl('', [Validators.required]),
    erscheinungsjahr: new FormControl('', [Validators.required]),
  })

  public formBookToUser: FormGroup = new FormGroup({
    book: new FormControl('', [Validators.required]),
  })

  prepareAddBookRequest(request: AddBookRequest) {
    request.titel = this.form.get("titel")?.value
    request.erscheinungsjahr = this.form.get("erscheinungsjahr")?.value
    return request;
  }

  addBook(): void {
    let request = new AddBookRequest()
    this.prepareAddBookRequest(request)
    this.booksFacade.addBook(request);
  }

  deleteBook(element: any): void {
    let request = new DeleteBookRequest()
    let elementValues = Object.keys(element).map(key => element[key])
    request.id = elementValues[0]
    this.booksFacade.deleteBook(request)
    this.subscriptionManager.add(this.booksFacade.stateGetBooksResponse$).subscribe(result => {
      if (result.length === 0) {
        this.booksListAvailable = false
      }
    })
  }

  assignBook(): void {
    let request = this.formBookToUser.get("book")?.value
    this.booksFacade.assignBookToUser(request)
  }

  deleteBooks(): void {
    this.booksFacade.deleteLogs()
  }
}
