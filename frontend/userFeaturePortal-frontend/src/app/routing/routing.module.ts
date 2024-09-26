import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule, Routes} from "@angular/router";
import {BmiComponent} from "../components/bmi/bmi.component";
import {UserComponent} from "../components/user/user.component";
import {LoggingComponent} from "../components/logging/logging.component";
import {HomeComponent} from "../components/home/home.component";
import {BookComponent} from "../components/book/book.component";
import {BibleComponent} from "../components/bibellese/bible.component";
import {LoginComponent} from "../components/navigation/login/login.component";

const routesLogin: Routes = [
  {path: 'login', component: LoginComponent}
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

const routesBible: Routes = [
  {path: 'bible', component: BibleComponent}
];

const different: Routes = [
  {path: '**', redirectTo: 'login'},
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
    RouterModule.forChild(routesBible),
    RouterModule.forChild(routesLogin),
    RouterModule.forChild(different)
  ],
  exports: [
    CommonModule,
    RouterModule
  ]
})
export class RoutingModule {
}
