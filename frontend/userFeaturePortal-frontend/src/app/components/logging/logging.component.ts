import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {LogFacade} from "../../modules/logging/logs.facade";
import {SubscriptionManager} from "../../../assets/utils/subscription.manager";
import {MatTableDataSource} from "@angular/material/table";
import {MatSnackBar} from "@angular/material/snack-bar";
import {AddLogRequest} from "../../modules/logging/addLogs/dto/add-log-request";
import {DeleteLogRequest} from "../../modules/logging/deleteLog/dto/delete-log-request";
import {MatPaginator} from "@angular/material/paginator";


@Component({
  selector: 'app-logging',
  templateUrl: './logging.component.html',
  styleUrls: ['./logging.component.scss']
})
export class LoggingComponent implements OnInit, OnDestroy {

  constructor(private logsFacade: LogFacade, private _snackBar: MatSnackBar) {
  }

  subscriptionManager = new SubscriptionManager();

  displayedColumns: string[] = ['id', 'message', 'severity', 'timestamp', 'user', 'delete'];
  severities: string[] = ['TRACE', 'DEBUG', 'INFO', 'WARNING', 'ERROR', 'FATAL'];
  dataSource: any;
  users: any

  @ViewChild(MatPaginator) paginator: MatPaginator | undefined;

  ngOnInit(): void {
    this.getLogs()
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
    this.logsFacade.deleteLogs()
  }

  public form: FormGroup = new FormGroup({
    message: new FormControl('', [Validators.required]),
    severity: new FormControl('', [Validators.required]),
  })

  public formFilter: FormGroup = new FormGroup({
    severity: new FormControl(''),
    user: new FormControl('')
  })

  getLogs(): void {
    this.logsFacade.getLogs()
    this.subscriptionManager.add(this.logsFacade.stateGetLogsResponse$).subscribe(result => {
      this.dataSource = new MatTableDataSource(result)
      this.dataSource.paginator = this.paginator;
    })
  }

  prepareAddLogRequest(request: AddLogRequest) {
    request.message = this.form.get("message")?.value
    request.severity = this.form.get("severity")?.value
    return request;
  }

  createLog(): void {
    let request = new AddLogRequest
    this.prepareAddLogRequest(request)
    this.logsFacade.addLog(request);
  }

  deleteLog(element: any): void {
    let request = new DeleteLogRequest
    let elementValues = Object.keys(element).map(key => element[key])
    request.id = elementValues[0]
    this.logsFacade.deleteLog(request)
  }
}
