import {Component, OnInit, ViewChild} from "@angular/core";
import {MatTableDataSource} from "@angular/material/table";
import {SubscriptionManager} from "../../../assets/utils/subscription.manager";
import {MatPaginator} from "@angular/material/paginator";
import {BooksFacade} from "../../modules/books/books.facade";

@Component({
  selector: 'app-book',
  templateUrl: './book.component.html',
  styleUrls: ['./book.component.scss']
})

export class BookComponent implements OnInit {

  constructor(private booksFacade: BooksFacade) {
  }

  subscriptionManager = new SubscriptionManager();

  displayedColumns: string[] = ['id', 'titel', 'erscheinungsjahr'];

  dataSource: any;

  @ViewChild(MatPaginator) paginator: MatPaginator | undefined;

  ngOnInit() {
    this.getBooks()
  }

  getBooks(): void {
    this.booksFacade.getBooks()
    this.subscriptionManager.add(this.booksFacade.stateGetBooksResponse$).subscribe(result => {
      this.dataSource = new MatTableDataSource(result)
      this.dataSource.paginator = this.paginator;
    })
  }

}
