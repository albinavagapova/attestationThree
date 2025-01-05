import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//Получить список задач
public class ToDoListTest {

    private final static String URL = "https://todo-app-sky.herokuapp.com/";

    private final static int CODE_OK = 200;

    private HttpClient httpClient;

    @BeforeEach
    public void setUp(){
        httpClient = HttpClientBuilder.create().build();
    }

    @Test
    public void getTasksCodeOk() throws IOException {
        HttpGet httpGet = new HttpGet(URL);
        HttpResponse httpResponse = httpClient.execute(httpGet);
        assertEquals(CODE_OK, httpResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void getTasksJson() throws IOException {
        HttpGet httpGet = new HttpGet(URL);
        HttpResponse httpResponse = httpClient.execute(httpGet);
        Header contentType = httpResponse.getFirstHeader("Content-Type");
        assertEquals("application/json; charset=utf-8", contentType.getValue());
    }

    @Test
    public void getTasksBodyPart() throws IOException {
        HttpGet httpGet = new HttpGet(URL);
        HttpResponse httpResponse = httpClient.execute(httpGet);
        assertEquals("HTTP", httpResponse.getProtocolVersion().getProtocol());
    }
}