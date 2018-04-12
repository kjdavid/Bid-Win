import { ItemService } from './item.service';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { RouterModule, Routes } from '@angular/router';

import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { IndexComponent } from './index/index.component';
import { RegistrationComponent } from './registration/registration.component';
import { HeaderComponent } from './header/header.component';
import { AuthService } from './auth.service';
import { RegisterService } from './register.service';
import { SettingsComponent } from './user/settings/settings.component';
const appRoutes : Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'index', component: IndexComponent},
  {path: 'registration', component: RegistrationComponent},
  {path: 'user/settings', component: SettingsComponent},
  {path: '**', redirectTo: 'index'},
];
@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    IndexComponent,
    RegistrationComponent,
    HeaderComponent,
    SettingsComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    RouterModule.forRoot(
      appRoutes, {enableTracing: true}
    )
  ],
  providers: [AuthService, RegisterService, ItemService],//ide kell írni majd a Service-eket.
  bootstrap: [AppComponent]
})
export class AppModule { }
