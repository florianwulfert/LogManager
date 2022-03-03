import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormControl} from "@angular/forms";
import {LogFacade} from "../../modules/logging/logs.facade";
import {SubscriptionManager} from "../../../assets/utils/subscription.manager";
import {MatTableDataSource} from "@angular/material/table";


@Component({
  selector: 'app-logging',
  templateUrl: './logging.component.html',
  styleUrls: ['./logging.component.scss']
})
export class LoggingComponent implements OnInit, OnDestroy {

  constructor(private logsFacade: LogFacade) {
  }

  subscriptionManager = new SubscriptionManager();

  displayedColumns: string[] = ['id', 'message', 'severity', 'timestamp', 'user', 'delete'];
  listIsEmptyMessage: string = 'There are no logs to show!';
  dataSource: any;

  ngOnInit(): void {
    this.logsFacade.getLogs()
    this.subscriptionManager.add(this.logsFacade.stateGetLogsResponse$).subscribe(result => {
      this.dataSource = new MatTableDataSource(result)
    })
  }

  ngOnDestroy() {
    this.subscriptionManager.clear()
  }

  position = new FormControl('above');

}
