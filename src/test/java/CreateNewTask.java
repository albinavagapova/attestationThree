import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//Создание задачи
public class CreateNewTask {

    private final static String URL = "https://todo-app-sky.herokuapp.com/";

    private final static int CODE_OK = 200;

    private HttpClient httpClient;

    @BeforeEach
    public void setUp() {
        httpClient = HttpClientBuilder.create().build();
    }

    //Проверка кода ответа
    @Test
    public void createTaskCodeOk() throws IOException {
        HttpPost httpPost = new HttpPost(URL);
        String jsonBodyToSend = "{\n"
                + "    \"title\": \"Создание самой новой задачи\"\n"
                + "}";
        HttpEntity bodyToSendEntity = new StringEntity(jsonBodyToSend, ContentType.APPLICATION_JSON);
        httpPost.setEntity(bodyToSendEntity);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        assertEquals(CODE_OK, httpResponse.getStatusLine().getStatusCode());
    }

        //Проверить, содержится ли в теле слово "Создание"
        @Test
        public void testIncludeWord() throws IOException {
        HttpPost httpPost2 = new HttpPost(URL);
        String jsonBodyToSend2 = "{\n"
                    + "    \"title\": \"Создание еще одной задачи\"\n"
                    + "}";
        HttpEntity bodyToSendEntity2 = new StringEntity(jsonBodyToSend2, ContentType.APPLICATION_JSON);
        httpPost2.setEntity(bodyToSendEntity2);
        HttpResponse httpResponse2 = httpClient.execute(httpPost2);
        String responseString = EntityUtils.toString(httpResponse2.getEntity());
        assertTrue(responseString.contains("Создание"), "Нет слова 'Создание'");


    }
}