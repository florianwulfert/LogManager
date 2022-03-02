import {Component, OnInit} from '@angular/core';

export interface Log {
  id: string
  message: string
  severity: string
  timestamp: string
  user: string
}

const data: Log[] = [
  {
    id: '1',
    message: 'Test',
    severity: 'Info',
    timestamp: 'TestTimestamp',
    user: 'Florian'
  }

];

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
  dataSource = data
}
