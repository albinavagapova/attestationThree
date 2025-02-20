package attestation3;

import attestation3.ext.CreateCompanyRequest;
import attestation3.ext.CreateCompanyResponse;
import attestation3.helpers.ApiCompanyHelper;
import attestation3.helpers.AuthHelper;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Company {
    private final static String URL = "https://x-clients-be.onrender.com/";

    private static ApiCompanyHelper apiCompanyHelper;

    private static AuthHelper authHelper;

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = URL;
        apiCompanyHelper = new ApiCompanyHelper();
        authHelper = new AuthHelper();
        String userToken = authHelper.authAndGetToken("leonardo", "leads");
        RestAssured.requestSpecification = new RequestSpecBuilder().build().header("x-client-token", userToken);
    }

    // given
    // when
    // then
    @Test
    @DisplayName("Код ответа при получении списка компаний")
    public void getCompanyList() {
        given().log().all()  // ДАНО:
                .basePath("company")
                .when()     // КОГДА
                .get() // ШЛЕШЬ ГЕТ ЗАПРОС
                .then() // ТОГДА ПРОВЕРЬ СЛЕДУЮЩЕЕ:
                .statusCode(200)
                .header("Content-Type", "application/json; charset=utf-8");

//        RequestSpecification requestSpecification = given().baseUri(URL + "company");
//        Response response = requestSpecification.when().get();
//        ValidatableResponse validatableResponse = response
//            .then()
//            .statusCode(200)
//            .header("Content-Type","application/json; charset=utf-8");;
    }

    @Test
    @DisplayName("Создание компании")
    public void createCompany() {
        CreateCompanyResponse createCompanyResponse = apiCompanyHelper.createCompany();

        System.out.println(createCompanyResponse.id());
    }

    @Test
    @DisplayName("Удаление компании")
    public void deleteCompany() {
        CreateCompanyResponse createCompanyResponse = apiCompanyHelper.createCompany();
        int deletedObjectId = apiCompanyHelper.deleteCompany(createCompanyResponse.id());
        assertEquals(createCompanyResponse.id(), deletedObjectId);
    }

    @Test
    @Disabled("Разобразться с авторизацией")
    @DisplayName("Создание компании выдает код 401 за клиента")
    public void createCompanyWithClientUser() {
        String userToken = authHelper.authAndGetToken("stella", "sun-fairy");
        CreateCompanyRequest createCompanyRequest = new CreateCompanyRequest("Entity company", "with entity");

        String errorMessage = given()  // ДАНО:
                .basePath("company")
                .body(createCompanyRequest)
                .contentType(ContentType.JSON)
                .header("x-client-token", userToken)
                .when()     // КОГДА
                .post() // ШЛЕШЬ ПОСТ ЗАПРОС
                .then()
                .statusCode(403).extract().asPrettyString();
        System.out.println(errorMessage);
    }


    @Test
    @DisplayName("Получить компанию по id")
    public void getCompany() {
        int id = 100;
        given()  // ДАНО:
                .basePath("company")
                .when()     // КОГДА
                .get("{id}", id).prettyPrint(); // ШЛЕШЬ ГЕТ ЗАПРОС
    }
}
