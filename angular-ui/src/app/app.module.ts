import { APP_INITIALIZER, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { CommonModule, JsonPipe } from '@angular/common';
import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular';
import { environment } from 'src/environments/environment';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { LoginInfoComponent } from './login-info/login-info.component';

const COMPONENTS = [
  AppComponent,
  HomeComponent,
  LoginInfoComponent
]
// 应用初始化
const APPINIT_PROVIDES = [
  {
    provide: APP_INITIALIZER,
    useFactory: environment.initializeKeycloak,
    multi: true,
    deps: [KeycloakService]
  }
];

@NgModule({
  declarations: [
    ...COMPONENTS
  ],
  imports: [
    BrowserModule,
    CommonModule,
    AppRoutingModule,
    KeycloakAngularModule
  ],
  providers: [
    ...APPINIT_PROVIDES
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
