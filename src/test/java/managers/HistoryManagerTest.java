package managers;

import org.junit.jupiter.api.Test;
import ru.yandex.managers.HistoryManager;
import ru.yandex.managers.InMemoryHistoryManager;
import ru.yandex.managers.InMemoryTaskManager;
import ru.yandex.managers.TaskManager;
import ru.yandex.task.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {
    private final TaskManager taskManager = new InMemoryTaskManager();
    private final HistoryManager historyManager = new InMemoryHistoryManager();

    @Test
    void testHistoryIsEmpty(){
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        taskManager.addNewTask(task1);

        List<Task> openTask = taskManager.getHistoryManager().getHistory();

        assertNotNull(openTask, "История задач null");
        assertEquals(openTask.size(), 0, "Список не пустой");
    }

    @Test
    void testHistoryIsNotEmpty(){
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        taskManager.addNewTask(task1);
        taskManager.getTaskForId(task1.getId());

        List<Task> openTask = taskManager.getHistoryManager().getHistory();

        assertNotNull(openTask, "История задач null");
        assertEquals(openTask.size(), 1, "Список не пустой");
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

        assertEquals(3, openTask.size(), "Размер списка не корректен");
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

        assertEquals(2,openTask.size(),"Размер списка не изменился");
        assertEquals(openTask.get(0).getId(), task2.getId());
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

        assertNull(taskManager.getTaskForId(task3.getId()));
        assertEquals(2,openTask.size(),"Размер списка не изменился");
        assertEquals(openTask.get(1).getId(), task2.getId());
    }
}
