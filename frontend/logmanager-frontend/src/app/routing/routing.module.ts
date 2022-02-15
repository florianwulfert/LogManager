import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule, Routes} from "@angular/router";
import {AppComponent} from "../app.component";
import {BmiComponent} from "../components/bmi/bmi.component";
import {UserComponent} from "../components/user/user.component";
import {LoggingComponent} from "../components/logging/logging.component";

const routes: Routes = [
  {path: 'app', component: AppComponent}
];

const routesBmi: Routes = [
  {path: 'bmi', component: BmiComponent}
];

const routesUser: Routes = [
  {path: 'user', component: UserComponent}
];

const routesLogging: Routes = [
  {path: 'logging', component: LoggingComponent}
];

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    RouterModule.forRoot(routes),
    RouterModule.forChild(routesBmi),
    RouterModule.forChild(routesUser),
    RouterModule.forChild(routesLogging)
  ],
  exports: [
    CommonModule,
    RouterModule,
  ]
})
export class RoutingModule { }
