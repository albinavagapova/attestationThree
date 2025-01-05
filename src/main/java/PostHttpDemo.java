import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
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

public class PostHttpDemo {

    public static void main(String[] args) throws IOException {
        // Создать клиент
        HttpClient httpClient = HttpClientBuilder.create().build();
        // Создать запрос
        HttpPost httpPost = new HttpPost("https://todo-app-sky.herokuapp.com/");
        String jsonBodyToSend = "{\n"
                + "    \"title\": \"Новая задача2\"\n"
                + "}";
        HttpEntity bodyToSendEntity = new StringEntity(jsonBodyToSend, ContentType.APPLICATION_JSON);
        httpPost.setEntity(bodyToSendEntity);
        //httpPost.setHeader("Content-Type","application/json");


        // Отправить запрос
        HttpResponse httpResponse = httpClient.execute(httpPost);
        // Получить информацию про код ответа и протокол
        System.out.println(httpResponse.getStatusLine().getStatusCode());

//        // Получить тело запроса
        HttpEntity body = httpResponse.getEntity();
        String bodyString = EntityUtils.toString(body);
        System.out.println(bodyString);

    }
}