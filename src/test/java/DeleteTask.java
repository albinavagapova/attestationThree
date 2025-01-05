import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
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
public class DeleteTask {

    private final static String URL = "https://todo-app-sky.herokuapp.com/";

    private final static int CODE_OK = 200;

    private HttpClient httpClient;

    @BeforeEach
    public void setUp() {
        httpClient = HttpClientBuilder.create().build();
    }

//Проверить, что статус 200 при удалении таски

    String taskId = "10687";
    String deleteUrl = URL + taskId;

    @Test
    public void deleteTaskCodeOk() throws IOException {
        HttpDelete httpDelete = new HttpDelete(deleteUrl);
        HttpResponse httpResponse = httpClient.execute(httpDelete);
        assertEquals(CODE_OK, httpResponse.getStatusLine().getStatusCode());
    }

    //Проверить текст ответа при удалении таски

    @Test

    public void deleteTaskText() throws IOException {
        HttpDelete httpDelete2 = new HttpDelete(deleteUrl);
        HttpResponse httpResponse2 = httpClient.execute(httpDelete2);
        String responseString = EntityUtils.toString(httpResponse2.getEntity());
        assertTrue(responseString.contains("todo was deleted"), "Текст не найден");
    }

}