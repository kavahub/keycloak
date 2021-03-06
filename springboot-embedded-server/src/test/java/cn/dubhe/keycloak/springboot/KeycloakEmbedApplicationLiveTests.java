package cn.dubhe.keycloak.springboot;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.response.Response;

/**
 * 运行测试前，先启动本项目服务。
 * 
 * @author PinWei Wan
 * @since 17.0.1
 */
public class KeycloakEmbedApplicationLiveTests {

        public static final String JWT_CLIENT_SECRET = "uBDEGFhCa6Rhgmx8msC8zwdQp2mMADay";
        public static final String JWT_CLIENT = "confidential-client";
        public static final String REDIRECT_URL = "http://localhost:4200/";

        public static final String AUTHORIZE_CODE_URL = "http://localhost:9000/auth/realms/dubhe/protocol/openid-connect/auth?response_type=code&client_id="
                        + JWT_CLIENT + "&scope=profile&redirect_uri=";
        public static final String TOKEN_URL = "http://localhost:9000/auth/realms/dubhe/protocol/openid-connect/token";
        public static final String INTROSPECT_URL = "http://localhost:9000/auth/realms/dubhe/protocol/openid-connect/token/introspect";
        public static final String USER_INFO_URL = "http://localhost:9000/auth/realms/dubhe/protocol/openid-connect/userinfo";

        public static final String USERNAME = "zhangs";
        public static final String PASSWORD = "123";
        public static final String OIDC_DISCOVERY_URL = "http://localhost:9000/auth/realms/dubhe/.well-known/openid-configuration";

        @Test
        public void givenAuthorizationCodeGrant_whenObtainAccessToken_thenSuccess() {
                String accessToken = obtainTokens().accessToken;

                assertThat(accessToken).isNotBlank();
                System.out.println("accessToken: " + accessToken);
        }

        @Test
        public void givenRefreshTokenGrantAndValidRefreshToken_whenObtainAccess_thenSuccess() {

                final String tokenUrl = "http://localhost:9000/auth/realms/dubhe/protocol/openid-connect/token";
                String refreshToken = obtainTokens().refreshToken;
                assertThat(refreshToken).isNotBlank();

                Map<String, String> params = new HashMap<>();
                params.put("client_id", JWT_CLIENT);
                params.put("client_secret", JWT_CLIENT_SECRET);
                params.put("grant_type", "refresh_token");
                params.put("refresh_token", refreshToken);
                Response response = RestAssured
                                .given()
                                .formParams(params)
                                .post(tokenUrl);
                assertThat(response
                                .jsonPath()
                                .getString("access_token")).isNotBlank();
        }

        @Test
        public void givenPasswordGrant_whenObtainAccessToken_thenSuccess() {
                Map<String, String> params = new HashMap<>();
                params.put("client_id", JWT_CLIENT);
                params.put("client_secret", JWT_CLIENT_SECRET);
                params.put("grant_type", "password");
                params.put("username", USERNAME);
                params.put("password", PASSWORD);
                Response response = RestAssured
                                .given()
                                .formParams(params)
                                .post(TOKEN_URL);
                assertThat(response
                                .jsonPath()
                                .getString("access_token")).isNotBlank();
        }

        @Test
        public void givenAccessTokenWithProfileScope_whenGetUserProfile_thenUsernameIsMatched() {
                String accessToken = obtainTokens().accessToken;
                Response response = RestAssured
                                .given()
                                .header("Authorization", String.format("Bearer %s", accessToken))
                                .get(USER_INFO_URL);
                assertThat(response
                                .jsonPath()
                                .getString("preferred_username")).isEqualTo(USERNAME);
        }

        @Test
        public void givenAccessToken_whenIntrospect_thenTokenIsActive() {
                String accessToken = obtainTokens().accessToken;
                Response response = RestAssured
                                .given()
                                .header("Authorization",
                                                String.format("Basic %s",
                                                                new String(
                                                                                Base64.getEncoder().encode((JWT_CLIENT
                                                                                                + ":"
                                                                                                + JWT_CLIENT_SECRET)
                                                                                                .getBytes()))))
                                .formParam("token", accessToken)
                                .post(INTROSPECT_URL);
                assertThat(response
                                .jsonPath()
                                .getBoolean("active")).isTrue();
        }

        @Test
        public void whenServiceStartsAndLoadsRealmConfigurations_thenOidcDiscoveryEndpointIsAvailable() {
                Response response = RestAssured
                                .given()
                                .redirects()
                                .follow(false)
                                .get(OIDC_DISCOVERY_URL);

                assertThat(HttpStatus.OK.value()).isEqualTo(response.getStatusCode());
                System.out.println(response.asString());
                assertThat(response
                                .jsonPath()
                                .getMap("$.")).containsKeys("issuer", "authorization_endpoint", "token_endpoint",
                                                "userinfo_endpoint");
        }

        private Tokens obtainTokens() {
                final String authorizeUrl = AUTHORIZE_CODE_URL + REDIRECT_URL;
                // obtain authentication url with custom codes
                Response response = RestAssured
                                .given()
                                .redirects()
                                .follow(false)
                                .get(authorizeUrl);
                String authSessionId = response.getCookie("AUTH_SESSION_ID");
                String kcPostAuthenticationUrl = response
                                .asString()
                                .split("action=\"")[1].split("\"")[0].replace("&amp;", "&");

                // obtain authentication code and state
                response = RestAssured
                                .given()
                                .redirects()
                                .follow(false)
                                .cookie("AUTH_SESSION_ID", authSessionId)
                                .formParams("username", USERNAME, "password", PASSWORD, "credentialId", "")
                                .post(kcPostAuthenticationUrl);
                assertThat(HttpStatus.FOUND.value()).isEqualTo(response.getStatusCode());

                // extract authorization code
                String location = response.getHeader(HttpHeaders.LOCATION);
                String code = location.split("code=")[1].split("&")[0];

                // get access token
                Map<String, String> params = new HashMap<>();
                params.put("grant_type", "authorization_code");
                params.put("code", code);
                params.put("client_id", JWT_CLIENT);
                params.put("redirect_uri", REDIRECT_URL);
                params.put("client_secret", JWT_CLIENT_SECRET);
                response = RestAssured
                                .given()
                                .formParams(params)
                                .post(TOKEN_URL);
                return new Tokens(response
                                .jsonPath()
                                .getString("access_token"),
                                response
                                                .jsonPath()
                                                .getString("refresh_token"));
        }

        private static class Tokens {
                private final String accessToken;
                private final String refreshToken;

                public Tokens(String accessToken, String refreshToken) {
                        this.accessToken = accessToken;
                        this.refreshToken = refreshToken;
                }
        }

}
