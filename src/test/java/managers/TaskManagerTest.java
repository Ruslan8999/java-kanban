package managers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.yandex.managers.TaskManager;
import ru.yandex.task.Epic;
import ru.yandex.task.Subtask;
import ru.yandex.task.Task;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class TaskManagerTest <T extends TaskManager>{
    T taskManager;

    TaskManagerTest(T taskManager) {
        this.taskManager = taskManager;
    }

    @AfterEach
    void clearAll(){
        taskManager.deleteAll();
    }

    @Test
    void addNewTaskTest() {
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        taskManager.addNewTask(task1);

        assertEquals(task1, taskManager.getTaskForId(0));
        assertNotNull(task1);
        assertNull(taskManager.getTaskForId(1));
    }

    @Test
    void addNewTaskTestIsNull() {;
        taskManager.addNewTask(null);
        assertNull(taskManager.getTaskForId(0));
    }

    @Test
    void updateTaskTest() {
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        taskManager.addNewTask(task1);
        task1 = new Task("Обновленная Задача 1", "Описание задачи 2");
        taskManager.updateTask(task1);

        assertEquals(task1, taskManager.getTaskForId(0));
        assertNotNull(task1);
        assertNull(taskManager.getTaskForId(1));
    }

    @Test
    void updateTaskIfNullTest() {
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        taskManager.addNewTask(task1);
        task1 = new Task("Обновленная Задача 1", "Описание задачи 2");
        taskManager.updateTask(null);

        assertEquals("Задача 1", taskManager.getTaskForId(task1.getId()).getNameTask());
    }

    @Test
    void getALLTaskForIdTest() {
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask1 = new Subtask("Подзадача 1 эпика 1", "Описание подзадачи 1", epic1);
        taskManager.addNewTask(task1);
        taskManager.addNewTask(epic1);
        taskManager.addNewTask(subtask1);

        assertEquals(taskManager.getTaskForId(task1.getId()), task1, "Неверный возврат task");
        assertEquals(taskManager.getEpicForId(epic1.getId()), epic1, "Неверный возврат epic");
        assertEquals(taskManager.getSubtaskForId(subtask1.getId()), subtask1, "Неверный возврат subtask");
    }

    @Test
    void getALLTaskForIdTestIfEmpty() {
        assertNull(taskManager.getTaskForId(0));
        assertNull(taskManager.getEpicForId(0));
        assertNull(taskManager.getSubtaskForId(0));
    }

    @Test
    void printALLTest() {
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        taskManager.addNewTask(task1);

        assertEquals(taskManager.getTaskForId(task1.getId()).toString(),"Task{name='Задача 1', descriptionTask='Описание задачи 1', id=0, statusTask=NEW, tasksType=TASK, startDate=null, durationInMinutes=0}");
    }

    @Test
    void deleteALLTaskTest() {
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask1 = new Subtask("Подзадача 1 эпика 1", "Описание подзадачи 1", epic1);
        taskManager.addNewTask(task1);
        taskManager.addNewTask(epic1);
        taskManager.addNewTask(subtask1);
        taskManager.deleteAll();

        assertEquals(taskManager.getTasks().size(),0);
        assertEquals(taskManager.getEpics().size(),0);
        assertEquals(taskManager.getSubtasks().size(),0);
    }

    @Test
    void deleteALLTaskForIdTest() {
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask1 = new Subtask("Подзадача 1 эпика 1", "Описание подзадачи 1", epic1);
        taskManager.addNewTask(task1);
        taskManager.addNewTask(epic1);
        taskManager.addNewTask(subtask1);
        taskManager.deleteTaskForId(0);
        taskManager.deleteEpicForId(0);
        taskManager.deleteSubTaskForId(0);

        assertNull(taskManager.getTaskForId(0));
        assertNull(taskManager.getEpicForId(0));
        assertNull(taskManager.getSubtaskForId(0));
    }

    @Test
    void deleteALLTaskForIncorrectIdTest() {
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask1 = new Subtask("Подзадача 1 эпика 1", "Описание подзадачи 1", epic1);
        taskManager.addNewTask(task1);
        taskManager.addNewTask(epic1);
        taskManager.addNewTask(subtask1);
        taskManager.deleteTaskForId(2);
        taskManager.deleteEpicForId(5);
        taskManager.deleteSubTaskForId(8);

        assertNotNull((taskManager.getTaskForId(0)));
        assertNotNull(taskManager.getEpicForId(0));
        assertNotNull(taskManager.getSubtaskForId(0));
    }

    @Test
    void getPrioritizedTasksTest() {
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Task task2 = new Task("Задача 2", "Описание задачи 2");
        Task task3 = new Task("Задача 3", "Описание задачи 3");
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        taskManager.addNewTask(task3);
        Task firstTask = taskManager.getPrioritizedTasks().first();
        Task endTask = taskManager.getPrioritizedTasks().last();

        assertEquals(firstTask, task1);
        assertEquals(endTask, task3);
        assertEquals(taskManager.getPrioritizedTasks().size(), 3);
    }

    @Test
    void getPrioritizedTasksIfEmpty() {
        assertTrue(taskManager.getPrioritizedTasks().isEmpty());
    }

    @Test
    void startEndDateTimeTest() {
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

        assertEquals(epic1.getDurationInMinutes(),73);
        assertEquals(epic1.getEndDate(), LocalDateTime.of(2022,8,12,11,28));
    }

    @Test
    void getHistoryManagerTest(){
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        taskManager.addNewTask(task1);
        taskManager.getTaskForId(task1.getId());

        assertFalse(taskManager.getHistoryManager().getHistory().isEmpty());
    }

    @Test
    void getHistoryIfEmptyTest() {
        assertTrue(taskManager.getHistoryManager().getHistory().isEmpty());
    }
}
