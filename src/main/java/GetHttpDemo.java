import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class GetHttpDemo {

    // Метод
    // Урл
    // Заголовок
    // Тело (опционально)
    //
    // Что и это зачем
    // Как работает
    // Термины
    // как работать с этим вручную
    // как это автоматизировать (на Java)
    // идем в поисковик (api test java http)
    public static void main(String[] args) throws IOException {
        // Создать клиент
        HttpClient httpClient = HttpClientBuilder.create().build();
        // Создать запрос
        HttpGet httpGet = new HttpGet("https://todo-app-sky.herokuapp.com/");
        // Отправить запрос
        HttpResponse httpResponse = httpClient.execute(httpGet);
        // Получить информацию про код ответа и протокол
        System.out.println(httpResponse.getStatusLine().getStatusCode());
        System.out.println(httpResponse.getStatusLine().getProtocolVersion());
        System.out.println(httpResponse.getStatusLine().getReasonPhrase());

        // Получить тело запроса
        HttpEntity body = httpResponse.getEntity();
        String bodyString = EntityUtils.toString(body);
        ObjectMapper objectMapper = new ObjectMapper();
        ToDoEntity[] toDoEntity = objectMapper.readValue(bodyString, ToDoEntity[].class);
        Arrays.stream(toDoEntity).forEach(System.out::println);
        System.out.println(toDoEntity[0].getId());

        // Получить заголовки
        Header[] headers = httpResponse.getAllHeaders();
        Arrays.stream(headers).forEach(System.out::println);
    }
}