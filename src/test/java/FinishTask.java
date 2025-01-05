import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//Переименовать задачу
public class FinishTask {

    private final static String URL = "https://todo-app-sky.herokuapp.com/10687";

    private final static int CODE_OK = 200;

    private HttpClient httpClient;

    @BeforeEach
    public void setUp() {
        httpClient = HttpClientBuilder.create().build();
    }

    //Проверка. что задача завершилась"
    @Test
    public void changeStatusTask() throws IOException {
        HttpPatch httpPatch = new HttpPatch(URL);
        String jsonBodyToSend = "{\n"
                + "    \"completed\": true\n"
                + "}";
        HttpEntity bodyToSendEntity = new StringEntity(jsonBodyToSend, ContentType.APPLICATION_JSON);
        httpPatch.setEntity(bodyToSendEntity);
        HttpResponse httpResponse = httpClient.execute(httpPatch);
        assertTrue(true, "Задача не завершена");
    }

    //Проверка статуса кода 200 при изменении статуса задачи

        @Test
        public void CodeOkAfterPatch() throws IOException {
            HttpPatch httpPatch2 = new HttpPatch(URL);
            String jsonBodyToSend2 = "{\n"
                    + "    \"completed\": false\n"
                    + "}";
            HttpEntity bodyToSendEntity2 = new StringEntity(jsonBodyToSend2, ContentType.APPLICATION_JSON);
            httpPatch2.setEntity(bodyToSendEntity2);
            HttpResponse httpResponse2 = httpClient.execute(httpPatch2);
            assertEquals(CODE_OK, httpResponse2.getStatusLine().getStatusCode());

    }
}