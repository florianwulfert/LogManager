import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {LogFacade} from "../../modules/logging/logs.facade";
import {MatTableDataSource} from "@angular/material/table";
import {MatSnackBar} from "@angular/material/snack-bar";
import {AddLogRequest} from "../../modules/logging/addLogs/dto/add-log-request";
import {MatPaginator} from "@angular/material/paginator";
import {UsersFacade} from "../../modules/users/users.facade";
import {GetLogsRequest} from "../../modules/logging/getLogs/dto/getLogs-request";
import {Subject} from "rxjs";
import {takeUntil} from "rxjs/operators";

@Component({
  selector: 'app-logging',
  templateUrl: './logging.component.html',
  styleUrls: ['./logging.component.scss']
})
export class LoggingComponent implements OnInit, OnDestroy {

  constructor(private logsFacade: LogFacade, private _snackBar: MatSnackBar, private userFacade: UsersFacade) {
  }

  displayedColumns: string[] = ['id', 'message', 'severity', 'timestamp', 'user', 'delete'];
  severities: string[] = ['TRACE', 'DEBUG', 'INFO', 'WARNING', 'ERROR', 'FATAL'];
  dataSource: any;
  users: any
  messages: any
  filterButtonPressed: boolean = false
  onDestroy = new Subject()

  @ViewChild(MatPaginator) paginator: MatPaginator | undefined;

  ngOnInit(): void {
    this.getLogs()
    this.getUserList()
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
    this.logsFacade.stateGetLogsResponse$.pipe(takeUntil(this.onDestroy)).subscribe(result => {
      this.dataSource = new MatTableDataSource(result)
      this.dataSource.paginator = this.paginator
      this.messages = result
    })
  }

  prepareAddLogRequest(request: AddLogRequest) {
    request.message = this.form.get("message")?.value
    request.severity = this.form.get("severity")?.value
    return request;
  }

  createLog(): void {
    let getLogsRequest = new GetLogsRequest()
    this.prepareGetLogsRequest(getLogsRequest)
    let addLogRequest = new AddLogRequest
    this.prepareAddLogRequest(addLogRequest)
    this.logsFacade.addLog(addLogRequest);
  }

  deleteLog(element: any): void {
    let request = new GetLogsRequest()
    this.prepareGetLogsRequest(request)
    let elementValues = Object.keys(element).map(key => element[key])
    request.id = elementValues[0]
    this.logsFacade.deleteLog(request)
  }

  getUserList(): void {
    this.userFacade.getUsers();
    this.userFacade.stateGetUsersResponse$.pipe(takeUntil(this.onDestroy)).subscribe(result => {
      this.users = result
    });
  }

  filterLogs(): void {
    this.getLogs()
    this.filterButtonPressed = true
  }

}
