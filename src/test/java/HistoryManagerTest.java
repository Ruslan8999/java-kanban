import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.managers.HistoryManager;
import ru.yandex.managers.InMemoryHistoryManager;
import ru.yandex.managers.InMemoryTaskManager;
import ru.yandex.managers.TaskManager;
import ru.yandex.task.Epic;
import ru.yandex.task.Subtask;
import ru.yandex.task.Task;

import java.time.LocalDateTime;
import java.util.List;

public class HistoryManagerTest {
    private TaskManager taskManager = new InMemoryTaskManager();
    private HistoryManager historyManager = new InMemoryHistoryManager();

    @Test
    void testHistoryIsEmpty(){
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        taskManager.addNewTask(task1);

        List<Task> openTask = taskManager.getHistoryManager().getHistory();

        Assertions.assertNotNull(openTask, "История задач null");
        Assertions.assertEquals(openTask.size(), 0, "Список не пустой");
    }

    @Test
    void testHistoryIsNotEmpty(){
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        taskManager.addNewTask(task1);
        taskManager.getTaskForId(task1.getId());

        List<Task> openTask = taskManager.getHistoryManager().getHistory();

        Assertions.assertNotNull(openTask, "История задач null");
        Assertions.assertEquals(openTask.size(), 1, "Список не пустой");
        historyManager.getHistory().clear();
    }

    @Test
    void testDuplicationTaskInNewHistory() {
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Task task2 = new Task("Задача 2", "Описание задачи 2");
        Task task3 = new Task("Задача 3", "Описание задачи 3");
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        taskManager.addNewTask(task3);
        taskManager.getTaskForId(task1.getId());
        taskManager.getTaskForId(task2.getId());
        taskManager.getTaskForId(task3.getId());
        taskManager.getTaskForId(task3.getId());

        List<Task> openTask = taskManager.getHistoryManager().getHistory();

        Assertions.assertEquals(3, openTask.size(), "Размер списка не корректен");
    }

    @Test
    void testFirstTaskInNewHistory(){
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Task task2 = new Task("Задача 2", "Описание задачи 2");
        Task task3 = new Task("Задача 3", "Описание задачи 3");
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        taskManager.addNewTask(task3);
        taskManager.getTaskForId(task1.getId());
        taskManager.getTaskForId(task2.getId());
        taskManager.getTaskForId(task3.getId());

        taskManager.deleteTaskForId(task1.getId());

        List<Task> openTask = taskManager.getHistoryManager().getHistory();

        Assertions.assertEquals(2,openTask.size(),"Размер списка не изменился");
        Assertions.assertEquals(openTask.get(0).getId(), task2.getId());
    }

    @Test
    void testLastTaskInNewHistory(){
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Task task2 = new Task("Задача 2", "Описание задачи 2");
        Task task3 = new Task("Задача 3", "Описание задачи 3");
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        taskManager.addNewTask(task3);
        taskManager.getTaskForId(task1.getId());
        taskManager.getTaskForId(task2.getId());
        taskManager.getTaskForId(task3.getId());

        taskManager.deleteTaskForId(2);

        List<Task> openTask = taskManager.getHistoryManager().getHistory();

        Assertions.assertNull(taskManager.getTaskForId(task3.getId()));
        Assertions.assertEquals(2,openTask.size(),"Размер списка не изменился");
        Assertions.assertEquals(openTask.get(1).getId(), task2.getId());
    }
}
