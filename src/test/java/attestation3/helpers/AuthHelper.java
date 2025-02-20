package attestation3.helpers;

import attestation3.ext.AuthRequest;
import attestation3.ext.AuthResponse;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class AuthHelper {
    public String authAndGetToken(String username, String password) {

        AuthRequest authRequest = new AuthRequest(username, password);

        AuthResponse authResponse = given()
                .basePath("auth/login")
                .body(authRequest)
                .contentType(ContentType.JSON)
                .header("x-client-token", "")
                .when()
                .post()
                .as(AuthResponse.class);

        return authResponse.userToken();
    }
}
