import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {UserComponent} from './components/user/user.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {HeaderComponent} from "./header/header.component";
import {MatSidenavModule} from "@angular/material/sidenav";
import {MenuComponent} from './menu/menu.component';
import {MatListModule} from "@angular/material/list";
import {MatMenuModule} from "@angular/material/menu";
import {LayoutComponent} from './layout/layout.component';
import {BmiComponent} from './components/bmi/bmi.component';
import {LoggingComponent} from './components/logging/logging.component';
import {RoutingModule} from './routing/routing.module';

@NgModule({
  declarations: [
    AppComponent,
    UserComponent,
    HeaderComponent,
    MenuComponent,
    LayoutComponent,
    BmiComponent,
    LoggingComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatSidenavModule,
    MatListModule,
    MatMenuModule,
    RoutingModule
  ],
  exports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatSidenavModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
