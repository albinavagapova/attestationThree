import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
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

//Переименовать задачу
public class ChangeTaskName {

    private final static String URL = "https://todo-app-sky.herokuapp.com/";

    private final static int CODE_OK = 200;

    private HttpClient httpClient;

    @BeforeEach
    public void setUp() {
        httpClient = HttpClientBuilder.create().build();
    }
//Проверить, что статус 200 при переименовании таски

    String taskId = "10683";
    String patchUrl = URL + taskId;

    @Test
    public void changeNameCodeOk() throws IOException {
        HttpPatch httpPatch = new HttpPatch(patchUrl);
        String jsonBodyToSend = "{\n"
                + "    \"title\": \"Переименованная задача\"\n"
                + "}";
        HttpEntity bodyToSendEntity = new StringEntity(jsonBodyToSend, ContentType.APPLICATION_JSON);
        httpPatch.setEntity(bodyToSendEntity);
        HttpResponse httpResponse = httpClient.execute(httpPatch);
        assertEquals(CODE_OK, httpResponse.getStatusLine().getStatusCode());
    }

    //Проверить, содержится ли слово "задача"
    @Test
    public void includeWordAfterPatch() throws IOException {
        HttpPatch httpPatch2 = new HttpPatch(patchUrl);
        String jsonBodyToSend2 = "{\n"
                + "    \"title\": \"Еще одна переименованная задача\"\n"
                + "}";
        HttpEntity bodyToSendEntity2 = new StringEntity(jsonBodyToSend2, ContentType.APPLICATION_JSON);
        httpPatch2.setEntity(bodyToSendEntity2);
        HttpResponse httpResponse2 = httpClient.execute(httpPatch2);
        String responseString = EntityUtils.toString(httpResponse2.getEntity());
        assertTrue(responseString.contains("задача"), "Нет слова 'задача'");

    }
}