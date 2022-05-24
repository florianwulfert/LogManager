import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {UserComponent} from './components/user/user.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {HeaderComponent} from "./components/navigation/header/header.component";
import {MatSidenavModule} from "@angular/material/sidenav";
import {MenuComponent} from './components/navigation/menu/menu.component';
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
import {MatInputModule} from "@angular/material/input";
import {UsersModule} from "./modules/users/users.module";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {MatTooltipModule} from "@angular/material/tooltip";
import {LogModule} from "./modules/logging/logs.module";
import {BmiModule} from "./modules/bmi/bmi.module";
import {MatSelectModule} from "@angular/material/select";
import {MatDialogModule} from "@angular/material/dialog";
import {ProfileMenuComponent} from './components/navigation/profile-menu/profile-menu.component';
import {ActorModule} from "./modules/actor/actor.module"
import {ActorIsValidModule} from "./modules/actor/actorIsValid.module";
import {BookComponent} from "./components/book/book.component";
import {MatPaginatorModule} from "@angular/material/paginator";
import {BooksModule} from "./modules/books/books.module";
import {UserModule} from "./modules/user/user.module";
import {BibleComponent} from "./components/bibellese/bible.component";
import {BibelleseModule} from "./modules/bibellese/bibellese.module";
import {BookModule} from "./modules/book/book.module";

@NgModule({
  declarations: [
    AppComponent,
    UserComponent,
    HeaderComponent,
    MenuComponent,
    BmiComponent,
    LoggingComponent,
    HomeComponent,
    ProfileMenuComponent,
    BookComponent,
    BibleComponent
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
    UsersModule,
    ReactiveFormsModule,
    MatSnackBarModule,
    MatTooltipModule,
    MatDialogModule,
    FormsModule,
    MatTooltipModule,
    LogModule,
    ActorModule,
    LogModule,
    MatSelectModule,
    FormsModule,
    LogModule,
    BmiModule,
    MatPaginatorModule,
    ActorIsValidModule,
    BooksModule,
    BibelleseModule,
    UserModule,
    BookModule
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
