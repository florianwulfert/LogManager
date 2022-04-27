import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {LogFacade} from "../../modules/logging/logs.facade";
import {SubscriptionManager} from "../../../assets/utils/subscription.manager";
import {MatTableDataSource} from "@angular/material/table";
import {MatSnackBar} from "@angular/material/snack-bar";
import {AddLogRequest} from "../../modules/logging/addLogs/dto/add-log-request";
import {DeleteLogRequest} from "../../modules/logging/deleteLog/dto/delete-log-request";
import {MatPaginator} from "@angular/material/paginator";
import {UserFacade} from "../../modules/user/user.facade";
import {GetLogsRequest} from "../../modules/logging/getLogs/dto/getLogs-request";


@Component({
  selector: 'app-logging',
  templateUrl: './logging.component.html',
  styleUrls: ['./logging.component.scss']
})
export class LoggingComponent implements OnInit, OnDestroy {

  constructor(private logsFacade: LogFacade, private _snackBar: MatSnackBar, private userFacade: UserFacade) {
  }

  subscriptionManager = new SubscriptionManager();

  displayedColumns: string[] = ['id', 'message', 'severity', 'timestamp', 'user', 'delete'];
  severities: string[] = ['TRACE', 'DEBUG', 'INFO', 'WARNING', 'ERROR', 'FATAL'];
  dataSource: any;
  users: any
  messages: any
  filterButtonPressed: boolean = false

  @ViewChild(MatPaginator) paginator: MatPaginator | undefined;

  ngOnInit(): void {
    this.getLogs()
    this.getUserList()
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
    user: new FormControl(''),
    startDateTime: new FormControl(''),
    endDateTime: new FormControl(''),
    message: new FormControl('')
  })

  prepareGetLogsRequest(request: GetLogsRequest) {
    request.severity = this.formFilter.get("severity")?.value
    request.message = this.formFilter.get("message")?.value
    request.startDateTime = this.formFilter.get("startDateTime")?.value
    request.endDateTime = this.formFilter.get("endDateTime")?.value
    request.user = this.formFilter.get("user")?.value

  }

  getLogs(): void {
    let request = new GetLogsRequest()
    this.prepareGetLogsRequest(request)
    this.logsFacade.getLogs(request)
    this.subscriptionManager.add(this.logsFacade.stateGetLogsResponse$).subscribe(result => {
      this.dataSource = new MatTableDataSource(result)
      this.dataSource.paginator = this.paginator;
      this.messages = result
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

  getUserList(): void {
    this.userFacade.getUser();
    this.subscriptionManager.add(this.userFacade.stateGetUserResponse$).subscribe(result => {
      this.users = result
    });
  }

  filterLogs(): void {
    this.getLogs()
    this.filterButtonPressed = true
  }

}
