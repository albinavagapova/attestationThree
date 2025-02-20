package attestation3.helpers;

import attestation3.ext.CreateCompanyRequest;
import attestation3.ext.CreateCompanyResponse;
import attestation3.ext.CreateEmployeeRequest;
import attestation3.ext.CreateEmployeeResponse;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;

public class ApiEmployeeHelper {

    private AuthHelper authHelper;

    public void ApiCompanyHelper() {
        authHelper = new AuthHelper();
    }

    public static CreateEmployeeResponse createEmployee() {
        CreateEmployeeRequest createEmployeeRequest = new CreateEmployeeRequest(1,"Testoviy", "Sotrudnik", "Sergeevich", 217, "1324@mail.ru", "1345.ru", "89878896969", "2000-05-06",true);

        return given().log().all()  // ДАНО:
                .basePath("employee")
                .body(createEmployeeRequest)
                .contentType(ContentType.JSON)
                .when()     // КОГДА
                .post() // ШЛЕШЬ ПОСТ ЗАПРОС
                .then()
                .statusCode(201)
                .body("id", is(greaterThan(0)))
                .extract().as(CreateEmployeeResponse.class);
    }
}
