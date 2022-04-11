import {Component, OnDestroy, OnInit, ViewChild} from "@angular/core";
import {MatTableDataSource} from "@angular/material/table";
import {SubscriptionManager} from "../../../assets/utils/subscription.manager";
import {MatPaginator} from "@angular/material/paginator";
import {BooksFacade} from "../../modules/books/books.facade";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AddBookRequest} from "../../modules/books/addBooks/add-book-request";
import {MatSnackBar} from "@angular/material/snack-bar";
import {FeatureManager} from "../../../assets/utils/feature.manager";
import {DeleteBookRequest} from "../../modules/books/deleteBook/delete-book-request";

@Component({
  selector: 'app-book',
  templateUrl: './book.component.html',
  styleUrls: ['./book.component.scss']
})

export class BookComponent implements OnInit, OnDestroy {

  constructor(private booksFacade: BooksFacade, private _snackBar: MatSnackBar) {
  }

  subscriptionManager = new SubscriptionManager();
  featureManager = new FeatureManager(this._snackBar);
  displayedColumns: string[] = ['id', 'titel', 'erscheinungsjahr', 'delete'];

  dataSource: any;
  position = new FormControl('above');

  @ViewChild(MatPaginator) paginator: MatPaginator | undefined;

  ngOnInit() {
    this.getBooks()
  }

  ngOnDestroy() {
    this.subscriptionManager.clear()
  }

  getBooks(): void {
    this.booksFacade.getBooks()
    this.subscriptionManager.add(this.booksFacade.stateGetBooksResponse$).subscribe(result => {
      this.dataSource = new MatTableDataSource(result)
      this.dataSource.paginator = this.paginator;
    })
  }

  public form: FormGroup = new FormGroup({
    titel: new FormControl('', [Validators.required]),
    erscheinungsjahr: new FormControl('', [Validators.required]),
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
    this.getBooks()
  }

  deleteBook(element: any): void {
    let request = new DeleteBookRequest()
    let elementValues = Object.keys(element).map(key => element[key])
    request.id = elementValues[0]
    this.booksFacade.deleteBook(request)
  }
}
