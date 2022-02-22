import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {UserComponent} from './components/user/user.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {HeaderComponent} from "./components/header/header.component";
import {MatSidenavModule} from "@angular/material/sidenav";
import {MenuComponent} from './components/menu/menu.component';
import {MatListModule} from "@angular/material/list";
import {MatMenuModule} from "@angular/material/menu";
import {BmiComponent} from './components/bmi/bmi.component';
import {LoggingComponent} from './components/logging/logging.component';
import {RoutingModule} from './routing/routing.module';
import {MatCardModule} from "@angular/material/card";
import {HomeComponent} from './components/home/home.component';
import {MatTableModule} from "@angular/material/table";
import {HttpClientModule} from "@angular/common/http";
import {MatExpansionModule} from "@angular/material/expansion";
import {MatFormFieldModule} from "@angular/material/form-field";
import {CreateUserComponent} from './components/user/create-user/create-user.component';
import {MatInputModule} from "@angular/material/input";
import {UserModule} from "./modules/user/user.module";

@NgModule({
  declarations: [
    AppComponent,
    UserComponent,
    HeaderComponent,
    MenuComponent,
    BmiComponent,
    LoggingComponent,
    HomeComponent,
    CreateUserComponent
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
    RoutingModule,
    MatCardModule,
    MatTableModule,
    HttpClientModule,
    MatExpansionModule,
    MatFormFieldModule,
    MatInputModule,
    UserModule
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
