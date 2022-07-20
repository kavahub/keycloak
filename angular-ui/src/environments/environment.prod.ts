import { KeycloakService } from "keycloak-angular";

export const environment = {
  production: true,
  // keycloak 初始化方法
  initializeKeycloak: function (keycloak: KeycloakService) {
    return () =>
      keycloak.init({
        config: {
          url: 'http://auth-server:9000/auth',
          realm: 'dubhe',
          clientId: 'angular-ui'
        },
        initOptions: {
          onLoad: 'check-sso',
          silentCheckSsoRedirectUri:
            window.location.origin + '/assets/silent-check-sso.html'
        },
        bearerExcludedUrls: ['/assets'],
      });
  }  
};
