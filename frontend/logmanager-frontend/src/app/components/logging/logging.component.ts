import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {LogsFacade} from "../../modules/logging/logs.facade";
import {SubscriptionManager} from "../../../assets/utils/subscription.manager";
import {MatTableDataSource} from "@angular/material/table";


export interface Log {
  id: string
  message: string
  severity: string
  timestamp: string
  user: string
}



@Component({
  selector: 'app-logging',
  templateUrl: './logging.component.html',
  styleUrls: ['./logging.component.scss']
})
export class LoggingComponent implements OnInit, OnDestroy {

  constructor(private logsFacade: LogsFacade) { }

  subscriptionManager = new SubscriptionManager();

  displayedColumns: string[] = ['id', 'message', 'severity', 'timestamp', 'user', 'delete'];
  listIsEmptyMessage: string = 'There are no logs to show!';
  dataSource : any;

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

  public form: FormGroup = new FormGroup({
    id: new FormControl(),
    message: new FormControl(),
    severity: new FormControl(),
    timestamp: new FormControl(),
    user: new FormControl()
  })

}
