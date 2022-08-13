package managers;

import org.junit.jupiter.api.Test;
import ru.yandex.managers.FileBackedTasksManager;
import ru.yandex.task.Epic;
import ru.yandex.task.Task;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    FileBackedTasksManagerTest() {
        super(new FileBackedTasksManager(new File("src/main/resources/tasks.csv")));
    }
    @Test
    void fileLoadFromFile() {
        FileBackedTasksManager fileBacked = new FileBackedTasksManager(new File("src/main/resources/tasks.csv"));
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        fileBacked.addNewTask(task1);
        fileBacked.getTaskForId(0);
        fileBacked.loadFromFile(new File("src/main/resources/tasks.csv"));

        assertEquals(fileBacked.getTaskForId(task1.getId()).toString(),"Task{name='Задача 1', descriptionTask='Описание задачи 1', id=0, statusTask=NEW, tasksType=TASK, startDate=null, durationInMinutes=0}");
    }

    @Test
    void fileOneTaskWithoutHistory() {
        FileBackedTasksManager fileBacked = new FileBackedTasksManager(new File("src/main/resources/tasks.csv"));
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        fileBacked.addNewTask(task1);

        assertEquals(fileBacked.getTasks().size(),1);
        assertEquals(fileBacked.getHistoryManager().getHistory().size(),0);
        fileBacked.deleteAll();
    }

    @Test
    void fileOneTaskWithHistory() {
        FileBackedTasksManager fileBacked = new FileBackedTasksManager(new File("src/main/resources/tasks.csv"));
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        fileBacked.addNewTask(task1);
        fileBacked.getTaskForId(task1.getId());

        assertEquals(fileBacked.getTasks().size(),1);
        assertEquals(fileBacked.getHistoryManager().getHistory().size(),1);
        fileBacked.deleteAll();
    }

    @Test
    void fileOneEpicWithoutSubtasks() {
        FileBackedTasksManager fileBacked = new FileBackedTasksManager(new File("src/main/resources/tasks.csv"));
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        fileBacked.addNewTask(epic1);

        assertEquals(fileBacked.getEpics().size(),1);
        assertEquals(fileBacked.getEpics().get(epic1.getId()),epic1);
        fileBacked.deleteAll();
    }
}
