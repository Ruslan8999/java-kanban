import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.managers.FileBackedTasksManager;
import ru.yandex.managers.TaskManager;
import ru.yandex.task.Epic;
import ru.yandex.task.Subtask;
import ru.yandex.task.Task;

import java.io.File;
import java.time.LocalDateTime;

abstract class TaskManagerTest <T extends TaskManager>{
    T taskManager;

    TaskManagerTest(T taskManager) {
        this.taskManager = taskManager;
    }

    @Test
    void addNewTask_TEST() {
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        taskManager.addNewTask(task1);

        Assertions.assertEquals(task1, taskManager.getTaskForId(0));
        Assertions.assertNotNull(task1);
        Assertions.assertNull(taskManager.getTaskForId(1));
        taskManager.deleteAll();
    }

    @Test
    void updateTask_TEST() {
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        taskManager.addNewTask(task1);
        task1 = new Task("Обновленная Задача 1", "Описание задачи 2");
        taskManager.updateTask(task1);

        Assertions.assertEquals(task1, taskManager.getTaskForId(0));
        Assertions.assertNotNull(task1);
        Assertions.assertNull(taskManager.getTaskForId(1));
        taskManager.deleteAll();
    }

    @Test
    void getALLTaskForId_TEST() {
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask1 = new Subtask("Подзадача 1 эпика 1", "Описание подзадачи 1", epic1);
        taskManager.addNewTask(task1);
        taskManager.addNewTask(epic1);
        taskManager.addNewTask(subtask1);

        Assertions.assertEquals(taskManager.getTaskForId(task1.getId()), task1, "Неверный возврат task");
        Assertions.assertEquals(taskManager.getEpicForId(epic1.getId()), epic1, "Неверный возврат epic");
        Assertions.assertEquals(taskManager.getSubtaskForId(subtask1.getId()), subtask1, "Неверный возврат subtask");
        taskManager.deleteAll();
    }

    @Test
    void deleteALLTask_TEST() {
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask1 = new Subtask("Подзадача 1 эпика 1", "Описание подзадачи 1", epic1);
        taskManager.addNewTask(task1);
        taskManager.addNewTask(epic1);
        taskManager.addNewTask(subtask1);
        taskManager.deleteAll();

        Assertions.assertEquals(taskManager.getTasks().size(),0);
        Assertions.assertEquals(taskManager.getEpics().size(),0);
        Assertions.assertEquals(taskManager.getSubtasks().size(),0);
    }

    @Test
    void deleteALLTaskForId_TEST() {
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask1 = new Subtask("Подзадача 1 эпика 1", "Описание подзадачи 1", epic1);
        taskManager.addNewTask(task1);
        taskManager.addNewTask(epic1);
        taskManager.addNewTask(subtask1);
        taskManager.deleteTaskForId(0);
        taskManager.deleteEpicForId(0);
        taskManager.deleteSubTaskForId(0);

        Assertions.assertNull(taskManager.getTaskForId(0));
        Assertions.assertNull(taskManager.getEpicForId(0));
        Assertions.assertNull(taskManager.getSubtaskForId(0));
    }

    @Test
    void getPrioritizedTasks_TEST() {
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Task task2 = new Task("Задача 2", "Описание задачи 2");
        Task task3 = new Task("Задача 3", "Описание задачи 3");
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        taskManager.addNewTask(task3);
        Task firstTask = taskManager.getPrioritizedTasks().first();
        Task endTask = taskManager.getPrioritizedTasks().last();

        Assertions.assertEquals(firstTask, task1);
        Assertions.assertEquals(endTask, task3);
        Assertions.assertEquals(taskManager.getPrioritizedTasks().size(), 3);
        taskManager.deleteAll();
    }

    @Test
    void startEndDateTime_TEST() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2022, 8, 12, 10, 15), 20);
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask1 = new Subtask("Подзадача 1 эпика 1", "Описание подзадачи 1", LocalDateTime.of(2022, 8, 12, 10, 15), 20, epic1);
        Subtask subtask2 = new Subtask("Подзадача 2 эпика 1", "Описание подзадачи 2", LocalDateTime.of(2022, 8, 12, 10, 15), 25, epic1);
        Subtask subtask3 = new Subtask("Подзадача 3 эпика 1", "Описание подзадачи 3", LocalDateTime.of(2022, 8, 12, 10, 15), 28, epic1);
        taskManager.addNewTask(task1);
        taskManager.addNewTask(epic1);
        taskManager.addNewTask(subtask1);
        taskManager.addNewTask(subtask2);
        taskManager.addNewTask(subtask3);

        Assertions.assertEquals(epic1.getDurationInMinutes(),73);
        Assertions.assertEquals(epic1.getEndDate(), LocalDateTime.of(2022,8,12,11,28));
    }

    @Test
    void fileOneTaskWithoutHistory_TEST() {
        FileBackedTasksManager fileBacked = new FileBackedTasksManager(new File("src/main/resources/tasks.csv"));
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        fileBacked.addNewTask(task1);

        Assertions.assertEquals(fileBacked.getTasks().size(),1);
        Assertions.assertEquals(fileBacked.getHistoryManager().getHistory().size(),0);
        fileBacked.deleteAll();
    }

    @Test
    void fileOneTaskWithHistory_TEST() {
        FileBackedTasksManager fileBacked = new FileBackedTasksManager(new File("src/main/resources/tasks.csv"));
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        fileBacked.addNewTask(task1);
        fileBacked.getTaskForId(task1.getId());

        Assertions.assertEquals(fileBacked.getTasks().size(),1);
        Assertions.assertEquals(fileBacked.getHistoryManager().getHistory().size(),1);
        fileBacked.deleteAll();
    }

    @Test
    void fileOneEpicWithoutSubtasks_TEST() {
        FileBackedTasksManager fileBacked = new FileBackedTasksManager(new File("src/main/resources/tasks.csv"));
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        fileBacked.addNewTask(epic1);

        Assertions.assertEquals(fileBacked.getEpics().size(),1);
        Assertions.assertEquals(fileBacked.getEpics().get(epic1.getId()),epic1);
        fileBacked.deleteAll();
    }
}
