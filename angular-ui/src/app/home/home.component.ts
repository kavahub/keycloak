import { Component, OnInit } from "@angular/core";
import { KeycloakService } from "keycloak-angular";

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit {
    title = "Angualr UI"
    isLoggedIn = false;

    constructor(private readonly keycloak: KeycloakService) {}
    
    public async ngOnInit() {
      this.keycloak.isLoggedIn().then(e => this.isLoggedIn = e);
    }
  
    public login() {
      this.keycloak.login();
    }
  
    public logout() {
      this.keycloak.logout();
    }  
}  