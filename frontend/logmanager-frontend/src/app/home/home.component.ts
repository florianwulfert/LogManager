import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

  bmiDescription = 'Berechnen Sie Ihren BMI anhand ihres Gewichts und ihrer Größe oder lassen Sie sich den BMI einer anderen Person anzeigen.'
  userDescription = 'Hier können Sie nach einem User suchen, ihn anlegen oder löschen.'
  loggingDescription = 'Hier können Sie die durchgeführten Aktionen im User Manager nachvollziehen.'
}
