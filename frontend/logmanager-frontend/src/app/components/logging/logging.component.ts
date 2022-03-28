import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {LogFacade} from "../../modules/logging/logs.facade";
import {SubscriptionManager} from "../../../assets/utils/subscription.manager";
import {MatTableDataSource} from "@angular/material/table";
import {FeatureManager} from "../../../assets/utils/feature.manager";
import {MatSnackBar} from "@angular/material/snack-bar";
import {AddLogRequest} from "../../modules/logging/addLogs/dto/add-log-request";


@Component({
  selector: 'app-logging',
  templateUrl: './logging.component.html',
  styleUrls: ['./logging.component.scss']
})
export class LoggingComponent implements OnInit, OnDestroy {

  constructor(private logsFacade: LogFacade, private _snackBar: MatSnackBar) {
  }

  subscriptionManager = new SubscriptionManager();
  returnUserMessage: string | undefined;
  featureManager = new FeatureManager(this._snackBar);

  displayedColumns: string[] = ['message', 'severity', 'timestamp', 'user', 'delete'];
  severities: string[] = ['TRACE', 'DEBUG', 'INFO', 'WARNING', 'ERROR', 'FATAL'];
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

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  deleteLogs(): void {
    this.logsFacade.deleteLogs();
    this.subscriptionManager.add(this.logsFacade.stateDeleteLogs$).subscribe(result => {
      this.returnUserMessage = result
    })
    this.featureManager.openSnackbar(this.returnUserMessage);
  }

  public form: FormGroup = new FormGroup({
    message: new FormControl('', [Validators.required]),
    severity: new FormControl('', [Validators.required]),
  })

  prepareAddLogRequest(request: AddLogRequest) {
    request.message = this.form.get("message")?.value
    request.severity = this.form.get("severity")?.value
    return request;
  }

  createLog(): void {
    let request = new AddLogRequest
    this.prepareAddLogRequest(request)
    this.logsFacade.addLog(request);
    this.subscriptionManager.add(this.logsFacade.stateAddLog$).subscribe(result => {
      this.returnUserMessage = result
    })
    this.featureManager.openSnackbar(this.returnUserMessage);
  }
}
