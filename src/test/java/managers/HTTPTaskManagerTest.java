package managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.http.HTTPTaskManager;
import ru.yandex.http.HttpTaskServer;
import ru.yandex.http.KVServer;
import ru.yandex.http.LocalDateTimeAdapter;
import ru.yandex.task.Epic;
import ru.yandex.task.Subtask;
import ru.yandex.task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HTTPTaskManagerTest {
    private static final int PORT = 8080;
    private String url = "http://localhost:" + PORT;
    private HTTPTaskManager taskManager;
    private HttpClient client;
    private HttpTaskServer httpTaskServer;
    private KVServer kvServer;
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    @BeforeEach
    void startServers() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        taskManager = new HTTPTaskManager(false);
        client = HttpClient.newHttpClient();
        httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
        taskManager.deleteAll();
    }

    @AfterEach
    void stopServers() {
        httpTaskServer.stop();
        kvServer.stop();
    }

    @Test
    void getTasks() throws IOException, InterruptedException {
        Task task1 = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2022, 8, 12, 10, 25), 20);
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask1 = new Subtask("Подзадача 1 эпика 1", "Описание подзадачи 1", LocalDateTime.of(2022, 8, 12, 11, 25), 20, epic1);
        Subtask subtask2 = new Subtask("Подзадача 2 эпика 1", "Описание подзадачи 2", LocalDateTime.of(2022, 8, 12, 10, 25), 20, epic1);
        Subtask subtask3 = new Subtask("Подзадача 3 эпика 1", "Описание подзадачи 3", LocalDateTime.of(2022, 8, 12, 10, 25), 20, epic1);
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");

        taskManager.addNewTask(task1);
        taskManager.addNewTask(epic1);
        taskManager.addNewTask(subtask1);
        taskManager.addNewTask(subtask2);
        taskManager.addNewTask(subtask3);
        taskManager.addNewTask(epic2);

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "/tasks/task/"))
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);

        List<Task> searchedTasks = gson.fromJson(response.body(), new TypeToken<List<Task>>() {
        }.getType());

        HashMap<Integer, Task> searchedTask = new HashMap<>();
        for (Task elem : searchedTasks) {
            searchedTask.put(elem.getId(), elem);
        }

        assertEquals(200, response.statusCode(), "Ошибка");
        assertEquals(searchedTask, taskManager.getTasks(), "Разные списки задач");
    }
}
