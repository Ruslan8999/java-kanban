import ru.yandex.managers.FileBackedTasksManager;

import java.io.File;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    FileBackedTasksManagerTest() {
        super(new FileBackedTasksManager(new File("src/main/resources/tasks.csv")));
    }
}
