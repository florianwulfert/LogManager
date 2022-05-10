import {Component, OnDestroy, OnInit, ViewChild} from "@angular/core";
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import {BooksFacade} from "../../modules/books/books.facade";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AddBookRequest} from "../../modules/books/addBooks/add-book-request";
import {MatSnackBar} from "@angular/material/snack-bar";
import {DeleteBookRequest} from "../../modules/books/deleteBook/delete-book-request";
import {UserFacade} from "../../modules/user/user.facade";
import {ActorFacade} from "../../modules/actor/actor.facade";
import {takeUntil} from "rxjs/operators";
import {Subject} from "rxjs";

@Component({
  selector: 'app-book',
  templateUrl: './book.component.html',
  styleUrls: ['./book.component.scss']
})

export class BookComponent implements OnInit, OnDestroy {

  constructor(private booksFacade: BooksFacade, private _snackBar: MatSnackBar, private usersFacade: UserFacade, private actorFacade: ActorFacade) {
  }

  displayedColumns: string[] = ['id', 'titel', 'erscheinungsjahr', 'delete'];
  books: any
  users: any
  userAvailable: boolean = false
  booksListAvailable: boolean = false
  onDestroy = new Subject()


  dataSource: any;

  @ViewChild(MatPaginator) paginator: MatPaginator | undefined;

  ngOnInit() {
    this.getBooks()
    this.actorFacade.stateActorIsValid$.pipe(takeUntil(this.onDestroy)).subscribe(r => {
      if (r) {
        this.userAvailable = true
      }
    })
    this.booksFacade.stateGetBooksResponse$.pipe(takeUntil(this.onDestroy)).subscribe(result => {
      if (result.length > 0) {
        this.booksListAvailable = true
      }
    })
  }

  ngOnDestroy() {
    this.onDestroy.next(null)
    this.onDestroy.complete()
  }

  position = new FormControl('above');

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  getBooks(): void {
    this.booksFacade.getBooks()
    this.booksFacade.stateGetBooksResponse$.pipe(takeUntil(this.onDestroy)).subscribe(result => {
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
    this.booksFacade.stateGetBooksResponse$.pipe(takeUntil(this.onDestroy)).subscribe(result => {
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
    this.booksFacade.deleteBooks()
  }
}
