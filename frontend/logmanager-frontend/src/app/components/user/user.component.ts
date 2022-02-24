import {Component, OnDestroy, OnInit} from '@angular/core';
import {UserFacade} from "../../modules/user/store/user.facade";
import {SubscriptionManager} from "../../../assets/utils/subscription.manager";
import {MatTableDataSource} from "@angular/material/table";

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit, OnDestroy {

  constructor(private userFacade: UserFacade) {
  }

  subscriptionManager = new SubscriptionManager();

  displayedColumns: string[] = ['id', 'name', 'birthdate', 'weight', 'height', 'favouriteColor', 'bmi', 'delete'];
  listIsEmptyMessage: string = 'There are no users to show!';
  dataSource: any;

  ngOnInit(): void {
    this.userFacade.getUser();
    this.subscriptionManager.add(this.userFacade.stateGetUserResponse$).subscribe(result => {
      this.dataSource = new MatTableDataSource(result)
    });
  }

  ngOnDestroy(): void {
    this.subscriptionManager.clear();
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }
}
