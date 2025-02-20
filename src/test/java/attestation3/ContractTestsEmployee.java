package attestation3;

import attestation3.ext.CreateCompanyResponse;
import attestation3.ext.CreateEmployeeResponse;
import attestation3.helpers.ApiCompanyHelper;
import attestation3.helpers.ApiEmployeeHelper;
import attestation3.helpers.AuthHelper;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ContractTestsEmployee {
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

    //создать компанию
    @Test
    @DisplayName("Создание компании")
    public void createCompany() {
        CreateCompanyResponse createCompanyResponse = apiCompanyHelper.createCompany();
        System.out.println(createCompanyResponse.id());
    }

    @Test
    @DisplayName("Получить компанию по id")
    public void getCompany() {
        int id = 217;
        given()  // ДАНО:
                .basePath("company")
                .when()     // КОГДА
                .get("{id}", id).prettyPrint(); // ШЛЕШЬ ГЕТ ЗАПРОС
    }

    //тесты сотрудников в новой компании
    //создание сотрудника
    @Test
    @DisplayName("Создание сотрудника")
    public void createEmployee() {
        CreateEmployeeResponse createEmployeeResponse = ApiEmployeeHelper.createEmployee();
        System.out.println(createEmployeeResponse.id());
    }
    //получение сотрудника

    @Test
    @DisplayName("Получить сотрудника по id")
    public void getEmployee() {
        int id = 225;
        given()  // ДАНО:
                .basePath("employee")
                .when()     // КОГДА
                .get("{id}", id).prettyPrint(); // ШЛЕШЬ ГЕТ ЗАПРОС
    }

    //получение сотрдуников по айди компании
    @Test
    @DisplayName("Получить список сотрудников по ID компании")
    public void getEmployeesByCompanyId() {
        int companyId = 217;
        Response response = given().log().all()
                .basePath("/employee")  // Базовый путь как в Swagger
                .queryParam("company", companyId)  // Используем query параметр "company"
                .when()
                .get()
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        System.out.println(response.prettyPrint());
    }

    //изменить данные сотрудника
    @Test
    @DisplayName("Изменить фамилию сотрудника по id")
    public void patchEmployee() {
        int id = 225;
        String newLastname = "Измененная";  // Указываем новую фамилию
        String requestBody = "{\n" +
                "  \"lastName\": \"" + newLastname + "\",\n" +
                "  \"email\": \"dfsdfds@mail.ru\",\n" +
                "  \"url\": \"sfsdfsf.mail.ru\",\n" +
                "  \"phone\": \"34535\",\n" +
                "  \"isActive\": true\n" +
                "}";


        given().log().all()
                .basePath("/employee")
                .pathParam("id", id)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .patch("/{id}")
                .then()
                .log().all()
                .statusCode(200);

        // Получение сотрдуника и проверка фамилии
        String newLastnameResponse = given().log().all()
                .basePath("/employee")
                .pathParam("id", id)
                .when()
                .get("/{id}")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .path("lastName");

        System.out.println("Updated Lastname: " + newLastnameResponse);
        assertEquals(newLastname, newLastnameResponse, "Фамилия сотрудника не изменилось.");
    }


    // Cоздание сотрудника с некорректными данными
    @Test
    @DisplayName("Создание сотрудника с некорректными данными")
    public void createEmployeeInvalidEmail() {
        String invalidRequestBody = "{\n" +
                "  \"firstName\": \"\",\n" +
                "  \"lastName\": \"\",\n" +
                "  \"email\": \"ddgsfgsgs\",\n" +  // Некорректный email
                "  \"phone\": \"\",\n" +
                "  \"birthdate\": \"465455\",\n" +
                "  \"isActive\": true\n" +
                "}";

        given().log().all()
                .basePath("/employee")
                .contentType("application/json")
                .body(invalidRequestBody)
                .when()
                .post()
                .then()
                .log().all()
                .statusCode(400);  // Ожидаем статус 400 Bad Request
    }

    //статус 200, хотя по документации д.б.404
    @Test
    @DisplayName("Получить сотрудника по несуществующему id")
    public void getNullEmployee() {
        int id = 000;
        given()
                .log().all()  // ДАНО:
                .basePath("/employee")
                .pathParam("id", id)
                .when()     // КОГДА
                .get("/{id}")
                .then()  // ПОСЛЕ
                .log().all()
                .statusCode(404);
    }

    //пустой массив в несуществующей компании
    @Test
    @DisplayName("Список сотрудников по несуществующей компании")
    public void getEmployeesByCompanyIdNull() {
        int companyIdNull = 000;  // Несуществующий ID компании

        Response response = given().log().all()
                .basePath("/employee")
                .queryParam("company", companyIdNull)
                .when()
                .get()
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();


        List<?> employees = response.jsonPath().getList("$");
        assertTrue(employees.isEmpty(), "Список сотрудников не пустой, хотя компании не существует.");


    }
}






