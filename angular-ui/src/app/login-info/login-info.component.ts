import { Component, OnInit } from "@angular/core";
import { KeycloakService } from "keycloak-angular";
import { KeycloakProfile } from "keycloak-js";

@Component({
    selector: 'app-login-info',
    templateUrl: './login-info.component.html'
})
export class LoginInfoComponent implements OnInit {
    userProfile: KeycloakProfile | null = null;

    constructor(private readonly keycloak: KeycloakService) { }

    public async ngOnInit() {
        this.keycloak.loadUserProfile().then(e => this.userProfile = e);
    }
}  