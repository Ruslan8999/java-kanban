package ru.yandex.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.yandex.managers.FileBackedTasksManager;
import ru.yandex.managers.HistoryManager;
import ru.yandex.managers.Managers;
import ru.yandex.task.Epic;
import ru.yandex.task.Subtask;
import ru.yandex.task.Task;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

public class HTTPTaskManager extends FileBackedTasksManager {
    private final KVTaskClient taskClient;
    private static final String KEY_TASKS = "tasks";
    private static final String KEY_SUBTASKS = "subtasks";
    private static final String KEY_EPICS = "epics";
    private static final String KEY_HISTORY = "history";

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public HTTPTaskManager(Boolean checkLoad) {
        super(new File("src/main/resources/tasks.csv"));
        taskClient = new KVTaskClient(KVServer.getServerURL());
        if (checkLoad) {
            load();
        }
    }

    @Override
    public void save() {
        if (!isNull(getTasks())) {
            taskClient.put(KEY_TASKS, gson.toJson(getTasks(), new TypeToken<Map<Integer, Task>>() {
            }.getType()));
        }
        if (!isNull(getEpics())) {
            taskClient.put(KEY_EPICS, gson.toJson(getEpics(), new TypeToken<Map<Integer, Epic>>() {
            }.getType()));
        }
        if (!isNull(getSubtasks())) {
            taskClient.put(KEY_SUBTASKS, gson.toJson(getSubtasks(), new TypeToken<Map<Integer, Subtask>>() {
            }.getType()));
        }
        if (!getHistoryManager().getHistory().isEmpty()) {
            taskClient.put(KEY_HISTORY, toStringHistory(Managers.getDefaultHistory()));
        }
    }

    public void load() {
        Map<Integer, Task> loadTasks = gson.fromJson(taskClient.load(KEY_TASKS), new TypeToken<Map<Integer, Task>>() {
        }.getType());
        for (Task task : loadTasks.values()) {
            getTasks().put(task.getId(), task);
        }
        Map<Integer, Epic> loadEpics = gson.fromJson(taskClient.load(KEY_EPICS), new TypeToken<Map<Integer, Epic>>() {
        }.getType());
        for (Epic epic : loadEpics.values()) {
            getEpics().put(epic.getId(), epic);
        }
        Map<Integer, Subtask> loadSubtasks = gson.fromJson(taskClient.load(KEY_SUBTASKS), new TypeToken<Map<Integer,
                Subtask>>() {
        }.getType());
        for (Subtask subtask : loadSubtasks.values()) {
            getSubtasks().put(subtask.getId(), subtask);
        }
        String[] historyFromServer = taskClient.load(KEY_HISTORY).trim().split(",");
        for (String number : historyFromServer) {
            if (!number.isBlank()) {
                getTaskForId(Integer.parseInt(number));
            }
        }
        for (Map.Entry<Integer, Task> entry : getTasks().entrySet()) {
            getPrioritizedTasks().add(entry.getValue());
        }
        for (Map.Entry<Integer, Subtask> entry : getSubtasks().entrySet()) {
            getPrioritizedTasks().add(entry.getValue());
        }
    }
}
