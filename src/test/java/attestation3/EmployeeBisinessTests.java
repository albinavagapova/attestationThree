package attestation3;

import attestation3.ext.CreateEmployeeResponse;
import attestation3.helpers.ApiCompanyHelper;
import attestation3.helpers.ApiEmployeeHelper;
import attestation3.helpers.AuthHelper;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class EmployeeBisinessTests {

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

    @Test
    @DisplayName("Создание сотрудника")
    public void createEmployee() {
        // Создание сотрудника
        CreateEmployeeResponse createEmployeeResponse = ApiEmployeeHelper.createEmployee();
        System.out.println("ID созданного сотрудника: " + createEmployeeResponse.id());

        // Проверка, что ID сотрудника не пустой
        assertNotNull(createEmployeeResponse.id(), "ID сотрудника не должен быть пустым");
        assertTrue(createEmployeeResponse.id() > 0, "ID сотрудника должен быть больше 0");
    }

    @Test
    @DisplayName("Создание сотрудника с только одним полем")
    public void createEmployeeWithOnlyOneField() {
        // Создание запроса с только одним полем
        String invalidRequestBody = "{\n" +
                "  \"firstName\": \"Testoviy\"\n" +  // Только имя
                "}";

        // POST запрос для создания сотрудника с только одним полем
        Response response = given().log().all()
                .basePath("/employee")
                .contentType(ContentType.JSON)
                .body(invalidRequestBody)
                .when()
                .post()
                .then()
                .log().all()
                .extract().response();
        // Не создан сотрдуник, если передавать только одно поле
        String employeeId = response.jsonPath().getString("id");
        assertNull(employeeId, "Сотрудник не должен быть создан с некорректными данными");
    }

    @Test
    @DisplayName("Получение сотрудника по существующему ID")
    public void getEmployeeByIdSuccess() {
        int employerId = 224;  // Существующий ID сотрудника

        // Запрос для получения сотрудника по ID
        Response response = given().log().all()
                .basePath("/employee")
                .pathParam("id", employerId)
                .when()
                .get("/{id}")
                .then()
                .log().all()
                .statusCode(200)  // Ожидаем статус 200 OK
                .extract().response();

        // Проверка, что получены данные сотрудника
        String lastName = response.jsonPath().getString("lastName");
        assertNotNull(lastName, "Фамилия сотрудника не должна быть пустой");
    }

    @Test
    @DisplayName("Получение сотрудника по несуществующему ID")
    public void getEmployeeByNull() {
        int employeeId = 0;

        // Запрос для получения сотрудника по ID
        Response response = given().log().all()
                .basePath("/employee")
                .pathParam("id", employeeId)
                .when()
                .get("/{id}")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        // Проверка, что тело ответа пустое
        assertTrue(response.getBody().asString().isEmpty(), "Ответ должен быть пустым");
    }


    @Test
    @DisplayName("Получение списка сотрудников по существующему ID компании")
    public void getEmployeesByCompanyIdSuccess() {
        int companyId = 217;  // Существующий ID компании

        // Запрос для получения списка сотрудников по ID компании
        Response response = given().log().all()
                .basePath("/employee")
                .queryParam("company", companyId)
                .when()
                .get()
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        // Проверка, что список сотрудников не пуст
        List<?> employees = response.jsonPath().getList("$");
        assertFalse(employees.isEmpty(), "Список сотрудников не должен быть пустым");
    }

    @Test
    @DisplayName("Получение списка сотрудников по несуществующему ID компании")
    public void getEmployeesByNonexistentCompanyId() {
        int noneCompanyId = 000;

        // Запрос для получения списка сотрудников по несуществующему ID компании
        Response response = given().log().all()
                .basePath("/employee")
                .queryParam("company", noneCompanyId)
                .when()
                .get()
                .then()
                .log().all()
                .statusCode(200)  // Ожидаем статус 200 OK
                .extract().response();

        // Проверка, что список сотрудников пуст
        List<?> employees = response.jsonPath().getList("$");
        assertTrue(employees.isEmpty(), "Приходит массив, хотя компании не существует");
    }
}


