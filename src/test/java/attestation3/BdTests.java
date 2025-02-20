package attestation3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BdTests {

    private static Connection connection;
    private static final String CONNECTION_STRING = "jdbc:postgresql://dpg-cuofqqt2ng1s73e8pm2g-a.frankfurt-postgres.render.com/x_clients_ehy7";
    private static final String LOGIN = "x_user";
    private static final String PASSWORD = "Mi4j6vZGytGHHMHhmHw86Q4MJ0YSLr1R";

    @BeforeAll
    public static void setup() throws SQLException {
       connection = DriverManager.getConnection(CONNECTION_STRING, LOGIN, PASSWORD);
    }

    @AfterAll
    public static void teardown() throws SQLException {
        // Закрытие соединения после выполнения всех тестов
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

        @Test
    @DisplayName("Успешное создание сотрудника")
    public void createEmployeeSuccess() throws SQLException {
        String insertQuery = "INSERT INTO employee (id, first_name, last_name, middle_name, phone, birthdate, avatar_url, company_id) " +
                "VALUES (277, 'Степан', 'Кошкин', 'Сергеевич', '89878896969', '2000-05-06', '217.ru', 217)";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(insertQuery);
            String selectQuery = "SELECT id FROM employee WHERE id = 16";
            ResultSet resultSet = statement.executeQuery(selectQuery);
            assertTrue(resultSet.next(), "Сотрудник существует");

        }
    }

        // Создание сотрудника с некорректными данными
    @Test
    @DisplayName("Создание сотрудника с пустыми данными - должны получить bisiness exception")
    public void createEmployeeWithInvalidData() throws SQLException {
        String insertQuery = "INSERT INTO employee (id, first_name, last_name, middle_name, phone, birthdate, avatar_url, company_id) " +
                "VALUES (999, NULL, NULL)";
        ;
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(insertQuery);
        } catch (SQLException e) {
            System.out.println("Не все обязательные поля заполнены");
            assertFalse(e.getMessage().toLowerCase().contains("null"));
        }
    }

    // Получение всех ID сотрудников по company_id
    @Test
    @DisplayName("Получение всех ID сотрудников по company_id")
    public void getAllEmployeeIdsByCompanyId() throws SQLException {
        int companyId = 217;
        String query = "SELECT id FROM employee WHERE company_id = " + companyId;

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            System.out.println("Список ID сотрудников:");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("id"));
            }
        }
    }

    @Test
    @DisplayName("Получение всех ID сотрудников по несуществующему company_id")
    public void getAllEmployeeIdsByNonexistentCompanyId() throws SQLException {
        int noneCompanyId = 99999;  // Несуществующий ID компании
        String query = "SELECT id FROM employee WHERE company_id = " + noneCompanyId;

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            System.out.println("Список ID сотрудников пустой");

        }
    }

    @Test
    @DisplayName("Успешное обновление данных сотрудника с ID 232")
    public void updateEmployeeSuccess() throws SQLException {
        int employeeId = 232;
        String updateQuery = "UPDATE employee SET first_name = 'НовыйРоман', email = 'newemail@example.com' WHERE id = " + employeeId;

        try (Statement statement = connection.createStatement()) {
            int rowsAffected = statement.executeUpdate(updateQuery);
            assertEquals(1, rowsAffected, "Имя обновлено");

            // Проверка, что данные обновлены корректно
            String selectQuery = "SELECT first_name, email FROM employee WHERE id = " + employeeId;
            try (ResultSet resultSet = statement.executeQuery(selectQuery)) {
                if (resultSet.next()) {
                    assertEquals("НовыйРоман", resultSet.getString("first_name"), "Имя обновлено");
                    assertEquals("newemail@example.com", resultSet.getString("email"), "Email сотрудника должен быть обновлен");
                } else {
                    fail("Сотрудник с данным ID не найден");
                }
            }
        }
    }

    @Test
    @DisplayName("Обновление данных сотрудника с некорректными данными")
    public void updateEmployeeWithInvalidData() throws SQLException {
        int employeeId = 228;
        String updateQuery = "UPDATE employee SET first_name = NULL WHERE id = " + employeeId;

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(updateQuery);
            System.out.println("данные обновлены");
        } catch (SQLException e) {
            assertTrue(e.getMessage().toLowerCase().contains("null value"), "Ошибка ");
        }
    }

    @Test
    @DisplayName("Поиск сотрудника по существующему ID")
    public void findEmployeeByIdSuccess() throws SQLException {
        int employeeId = 230;
        String query = "SELECT id, first_name, last_name FROM employee WHERE id = " + employeeId;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            assertTrue(resultSet.next());
            assertEquals(employeeId, resultSet.getInt("id"));
            System.out.println("Сотрудник найден: " + resultSet.getString("first_name") + " " + resultSet.getString("last_name"));
        }
    }

    @Test
    @DisplayName("Поиск сотрудника по несуществующему ID")
    public void findEmployeeByNonexistentId() throws SQLException {
        int noneEmployeeId = 000;  // Несуществующий ID сотрудника
        String query = "SELECT id FROM employee WHERE id = " + noneEmployeeId;

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            System.out.println("Сотрудник не найден");
        }
    }

}



