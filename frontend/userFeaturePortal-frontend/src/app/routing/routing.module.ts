import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule, Routes} from "@angular/router";
import {BmiComponent} from "../components/bmi/bmi.component";
import {UserComponent} from "../components/user/user.component";
import {LoggingComponent} from "../components/logging/logging.component";
import {HomeComponent} from "../components/home/home.component";
import {BookComponent} from "../components/book/book.component";

const routes: Routes = [
  {path: 'app', redirectTo: 'home'},
];

const routesBmi: Routes = [
  {path: 'bmi', component: BmiComponent}
];

const routesBook: Routes = [
  {path: 'book', component: BookComponent}
]

const routesUser: Routes = [
  {path: 'user', component: UserComponent}
];

const routesLogging: Routes = [
  {path: 'logging', component: LoggingComponent}
];

const routesHome: Routes = [
  {path: 'home', component: HomeComponent}
];

const different: Routes = [
  {path: '**', redirectTo: 'home'},
];

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    RouterModule.forChild(routesBmi),
    RouterModule.forChild(routesBook),
    RouterModule.forChild(routesUser),
    RouterModule.forChild(routesLogging),
    RouterModule.forChild(routesHome),
    RouterModule.forChild(different)
  ],
  exports: [
    CommonModule,
    RouterModule
  ]
})
export class RoutingModule {
}
