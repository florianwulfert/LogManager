import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-logging',
  templateUrl: './logging.component.html',
  styleUrls: ['./logging.component.scss']
})
export class LoggingComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

  displayedColumns: string[] = ['id', 'message', 'severity', 'timestamp', 'user', 'delete'];
  listIsEmptyMessage: string = 'There are no logs to show!';
  dataSource: any;

  public form: FormGroup = new FormGroup({
    id: new FormControl(),
    message: new FormControl(),
    severity: new FormControl(),
    timestamp: new FormControl(),
    user: new FormControl()
  })
}
